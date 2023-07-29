package com.infiniterealm.achievers.students.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.infiniterealm.achievers.R;

public class StudentProfileActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    FirebaseUser user;
    DatabaseReference mDbRef;
    TextView name, rollNumber, email, phone, parentPhone, dob, school;
    ProgressBar progressBar;
    LinearLayout rollNumberLayout, emailLayout, phoneLayout, parentPhoneLayout, dobLayout, schoolLayout, profileLayout;
    private ShapeableImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        constraintLayout = findViewById(R.id.studentProfilePage);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        name = findViewById(R.id.name);
        rollNumber = findViewById(R.id.roll_number);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        parentPhone = findViewById(R.id.parent_phone);
        dob = findViewById(R.id.dob);
        school = findViewById(R.id.school);

        profileImage = findViewById(R.id.profile_image);

        profileLayout = findViewById(R.id.profile_page_layout);
        emailLayout = findViewById(R.id.email_layout);
        rollNumberLayout = findViewById(R.id.roll_number_layout);
        phoneLayout = findViewById(R.id.phone_layout);
        parentPhoneLayout = findViewById(R.id.parent_phone_layout);
        dobLayout = findViewById(R.id.dob_layout);
        schoolLayout = findViewById(R.id.school_layout);

        mDbRef = FirebaseDatabase.getInstance().getReference("students");


        Intent intent = getIntent();
        String studentName = intent.getStringExtra("name");
        String studentID = intent.getStringExtra("ID");

        showSnackBar(constraintLayout, studentName + ": " + studentID);
    }

    private void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}