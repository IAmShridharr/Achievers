package com.infiniterealm.achievers.students.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infiniterealm.achievers.R;

import java.net.URLEncoder;

public class StudentProfileActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    ShapeableImageView profileImage;
    DatabaseReference mDbRef;
    Button complaint;
    TextView profileName, name, rollNumber, email, phone, parentPhone, dob, school;
    ProgressBar progressBar;
    LinearLayout rollNumberLayout, emailLayout, phoneLayout, parentPhoneLayout, dobLayout, schoolLayout, profileLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        constraintLayout = findViewById(R.id.studentProfilePage);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        name = findViewById(R.id.name);
        profileName = findViewById(R.id.profileName);
        rollNumber = findViewById(R.id.roll_number);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        parentPhone = findViewById(R.id.parent_phone);
        dob = findViewById(R.id.dob);
        school = findViewById(R.id.school);

        complaint = findViewById(R.id.complaint);

        profileImage = findViewById(R.id.student_profile_image);

        profileLayout = findViewById(R.id.profile_page_layout);
        emailLayout = findViewById(R.id.email_layout);
        rollNumberLayout = findViewById(R.id.roll_number_layout);
        phoneLayout = findViewById(R.id.phone_layout);
        parentPhoneLayout = findViewById(R.id.parent_phone_layout);
        dobLayout = findViewById(R.id.dob_layout);
        schoolLayout = findViewById(R.id.school_layout);

        Intent intent = getIntent();
        String studentName = intent.getStringExtra("name");
        String profileText = studentName + "'s Profile";
        profileName.setText(profileText);
        String studentID = intent.getStringExtra("ID");
        assert studentID != null;
        String std = studentID.substring(0,1);
        String standard;
        switch (std) {
            case "J":
                standard = "Jr. KG";
                break;
            case "S":
                standard = "Sr. KG";
                break;
            case "1":
                standard = "1st Standard";
                break;
            case "2":
                standard = "2nd Standard";
                break;
            case "3":
                standard = "3rd Standard";
                break;
            case "4":
                standard = "4th Standard";
                break;
            case "5":
                standard = "5th Standard";
                break;
            case "6":
                standard = "6th Standard";
                break;
            case "7":
                standard = "7th Standard";
                break;
            case "8":
                standard = "8th Standard";
                break;
            case "9":
                standard = "9th Standard";
                break;
            case "X":
                standard = "10th Standard";
                break;
            default:
                standard = "Pre-School";
                break;
        }

        mDbRef = FirebaseDatabase.getInstance().getReference("students").child(standard).child(studentID).child("Profile Information");

        getStudentData();

        complaint.setOnClickListener(view -> {
            Snackbar bar = Snackbar.make(profileLayout, "Opening WhatsApp...", Snackbar.LENGTH_LONG);
            bar.show();

            String phoneNumberWithCountryCode = "+918432312431";
            String message = "Hello Bhaiya, I have to complain about " + studentName + ".";

            try {
                PackageManager packageManager = getApplicationContext().getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                String url = "https://api.whatsapp.com/send?phone=" + phoneNumberWithCountryCode + "&text=" + URLEncoder.encode(message, "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));

                if (i.resolveActivity(packageManager) != null) {
                    getApplicationContext().startActivity(i);
                } else {
                    Snackbar snackbar = Snackbar.make(profileLayout, "WhatsApp not Installed!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(profileLayout, "WhatsApp not Installed!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void getStudentData() {
        mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String DP = snapshot.child("profileImageUrl").getValue(String.class);
                String Name = snapshot.child("name").getValue(String.class);
                String Email = snapshot.child("email").getValue(String.class);
                String RollNumber = snapshot.child("id").getValue(String.class);
                String Phone = snapshot.child("phone").getValue(String.class);
                String ParentPhone = snapshot.child("parentPhone").getValue(String.class);
                String DOB = snapshot.child("DOB").getValue(String.class);
                String School = snapshot.child("school").getValue(String.class);

                if (DP == null || DP.equals("")) {
                    profileImage.setImageResource(R.drawable.profile_picture_placeholder);
                } else {
                    Glide.with(getApplicationContext())
                            .load(DP)
                            .into(profileImage);
                }
                assert Name != null;
                if (Name.isEmpty()) {
                    String nameText = "No Name";
                    name.setText(nameText);
                } else {
                    name.setText(Name);
                }
                assert RollNumber != null;
                if (RollNumber.isEmpty()) {
                    rollNumberLayout.setVisibility(View.GONE);
                } else {
                    rollNumberLayout.setVisibility(View.VISIBLE);
                    rollNumber.setText(RollNumber);
                }
                assert Email != null;
                if (Email.isEmpty()) {
                    emailLayout.setVisibility(View.GONE);
                } else {
                    emailLayout.setVisibility(View.VISIBLE);
                    email.setText(Email);
                }
                assert Phone != null;
                if (Phone.isEmpty()) {
                    phoneLayout.setVisibility(View.GONE);
                } else {
                    phoneLayout.setVisibility(View.VISIBLE);
                    phone.setText(Phone);
                }
                assert ParentPhone != null;
                if (ParentPhone.isEmpty()) {
                    parentPhoneLayout.setVisibility(View.GONE);
                } else {
                    parentPhoneLayout.setVisibility(View.VISIBLE);
                    parentPhone.setText(ParentPhone);
                }
                assert DOB != null;
                if (DOB.isEmpty()) {
                    dobLayout.setVisibility(View.GONE);
                } else {
                    dobLayout.setVisibility(View.VISIBLE);
                    dob.setText(DOB);
                }
                assert School != null;
                if (School.isEmpty()) {
                    schoolLayout.setVisibility(View.GONE);
                } else {
                    schoolLayout.setVisibility(View.VISIBLE);
                    school.setText(School);
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar = Snackbar.make(profileLayout, "Something went wrong", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}