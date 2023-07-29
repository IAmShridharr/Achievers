package com.infiniterealm.achievers.admins.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.infiniterealm.achievers.R;

public class EditStudentProfilesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_profiles);

        Intent intent = getIntent();
        String ID = intent.getStringExtra("id");
        assert ID != null;

    }
}