package com.infiniterealm.achievers.students.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.utilities.Essentials;
import com.infiniterealm.achievers.utilities.SnackBarHelper;

public class StudentProfileActivity extends AppCompatActivity {

    String MyID;
    ConstraintLayout constraintLayout;
    ShapeableImageView profileImage;
    SharedPreferences sharedPreferences;
    DatabaseReference mDbRef;
    DatabaseReference myDBRef;
    MaterialButton complaint, follow, message;
    TextView profileName, name, rollNumber, dob, school, followers, followings, tests;
    ProgressBar progressBar;
    LinearLayout rollNumberLayout, dobLayout, schoolLayout, profileLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        message = findViewById(R.id.message);

        sharedPreferences = getApplicationContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        MyID = sharedPreferences.getString("id", null);
        assert MyID != null;
        String myStandard = Essentials.getStandard(MyID);

        constraintLayout = findViewById(R.id.studentProfilePage);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        name = findViewById(R.id.name);
        profileName = findViewById(R.id.profileName);
        rollNumber = findViewById(R.id.roll_number);
        dob = findViewById(R.id.dob);
        school = findViewById(R.id.school);

        followers = findViewById(R.id.followers);
        tests = findViewById(R.id.tests);
        followings = findViewById(R.id.followings);

        follow = findViewById(R.id.follow);
        complaint = findViewById(R.id.complaint);

        profileImage = findViewById(R.id.student_profile_image);

        profileLayout = findViewById(R.id.profile_page_layout);
        rollNumberLayout = findViewById(R.id.roll_number_layout);
        dobLayout = findViewById(R.id.dob_layout);
        schoolLayout = findViewById(R.id.school_layout);

        Intent intent = getIntent();
        String studentName = intent.getStringExtra("name");
        String profileText = studentName + "'s Profile";
        profileName.setText(profileText);
        String studentID = intent.getStringExtra("ID");
        assert studentID != null;
        String standard = Essentials.getStandard(studentID);

        myDBRef = FirebaseDatabase.getInstance().getReference("students").child(myStandard).child(MyID).child("Profile Information");
        mDbRef = FirebaseDatabase.getInstance().getReference("students").child(standard).child(studentID).child("Profile Information");

        getStudentData();

        complaint.setOnClickListener(view -> {
            SnackBarHelper.showShortSnackBar(profileLayout, "Opening WhatsApp...");

            String phoneNumberWithCountryCode = "+918432312431";
            String message = "Hello Bhaiya, I have to complain about " + studentName + ".";

            Essentials.openWhatsApp(phoneNumberWithCountryCode, message, getApplicationContext());
        });

        DatabaseReference followDataRef = FirebaseDatabase.getInstance().getReference().child("students").child(myStandard).child(MyID).child("Follow Data").child("Followings");

        followDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(studentID).exists()) {
                    String followButtonText = "Unfollow";
                    follow.setText(followButtonText);
                    follow.setIconResource(R.drawable.baseline_person_remove_24);
                    message.setEnabled(true);
                } else {
                    String followButtonText = "Follow";
                    follow.setText(followButtonText);
                    follow.setIconResource(R.drawable.baseline_person_add_24);
                    message.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        message.setOnClickListener(view -> SnackBarHelper.showShortSnackBar(view, "Message is clicked!"));

        follow.setOnClickListener(view -> myDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int myFollowers, myFollowings, myNewFollowers, myNewFollowings;
                String MyFollowers, MyFollowings, MyNewFollowers, MyNewFollowings;
                MyFollowers = String.valueOf(snapshot.child("followers").getValue(Integer.class));
                MyFollowings = String.valueOf(snapshot.child("followings").getValue(Integer.class));
                myFollowers = Integer.parseInt(MyFollowers);
                myFollowings = Integer.parseInt(MyFollowings);

                if (follow.getText().toString().equals("Follow")) {
                    myNewFollowings = myFollowings+1;
                    MyNewFollowings = String.valueOf(myNewFollowings);
                    myDBRef.child("followings").setValue(myNewFollowings);

                    mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int theirFollowers, theirFollowings, theirNewFollowers, theirNewFollowings;
                            String TheirFollowers, TheirFollowings, TheirNewFollowers, TheirNewFollowings;

                            TheirFollowers = String.valueOf(snapshot.child("followers").getValue(Integer.class));
                            TheirFollowings = String.valueOf(snapshot.child("followings").getValue(Integer.class));

                            theirFollowers = Integer.parseInt(TheirFollowers);
                            theirFollowings = Integer.parseInt(TheirFollowings);

                            theirNewFollowers = theirFollowers+1;
                            TheirNewFollowers = String.valueOf(theirNewFollowers);

                            mDbRef.child("followers").setValue(theirNewFollowers);

                            followers.setText(TheirNewFollowers);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    DatabaseReference theirNewRef = FirebaseDatabase.getInstance().getReference("students").child(standard).child(studentID).child("Follow Data");
                    theirNewRef.child("Followers").child(MyID).setValue(true);

                    DatabaseReference myNewRef = FirebaseDatabase.getInstance().getReference("students").child(myStandard).child(MyID).child("Follow Data");
                    myNewRef.child("Followings").child(studentID).setValue(true);

                    String changedText = "Unfollow";
                    follow.setText(changedText);
                    follow.setIconResource(R.drawable.baseline_person_remove_24);
                    message.setEnabled(true);
                } else {
                    myNewFollowings = myFollowings-1;
                    MyNewFollowings = String.valueOf(myNewFollowings);
                    myDBRef.child("followings").setValue(myNewFollowings);

                    mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int theirFollowers, theirFollowings, theirNewFollowers, theirNewFollowings;
                            String TheirFollowers, TheirFollowings, TheirNewFollowers, TheirNewFollowings;

                            TheirFollowers = String.valueOf(snapshot.child("followers").getValue(Integer.class));
                            TheirFollowings = String.valueOf(snapshot.child("followings").getValue(Integer.class));

                            theirFollowers = Integer.parseInt(TheirFollowers);
                            theirFollowings = Integer.parseInt(TheirFollowings);

                            theirNewFollowers = theirFollowers-1;
                            TheirNewFollowers = String.valueOf(theirNewFollowers);

                            mDbRef.child("followers").setValue(theirNewFollowers);

                            followers.setText(TheirNewFollowers);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    DatabaseReference theirNewRef = FirebaseDatabase.getInstance().getReference("students").child(standard).child(studentID).child("Follow Data");
                    theirNewRef.child("Followers").child(MyID).removeValue();

                    DatabaseReference myNewRef = FirebaseDatabase.getInstance().getReference("students").child(myStandard).child(MyID).child("Follow Data");
                    myNewRef.child("Followings").child(studentID).removeValue();

                    String changedText = "Follow";
                    follow.setText(changedText);
                    follow.setIconResource(R.drawable.baseline_person_add_24);
                    message.setEnabled(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
    }

    private void getStudentData() {
        mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String DP = snapshot.child("profileImageUrl").getValue(String.class);
                String Name = snapshot.child("name").getValue(String.class);
                String RollNumber = snapshot.child("id").getValue(String.class);
                String DOB = snapshot.child("DOB").getValue(String.class);
                String School = snapshot.child("school").getValue(String.class);
                String Followers = String.valueOf(snapshot.child("followers").getValue(Integer.class));
                String Tests = String.valueOf(snapshot.child("tests").getValue(Integer.class));
                String Followings = String.valueOf(snapshot.child("followings").getValue(Integer.class));

                followers.setText(Followers);
                followings.setText(Followings);
                tests.setText(Tests);

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
                SnackBarHelper.showShortSnackBar(profileLayout, "Something went wrong!");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}