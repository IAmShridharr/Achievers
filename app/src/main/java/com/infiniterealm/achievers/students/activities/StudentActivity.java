package com.infiniterealm.achievers.students.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.infiniterealm.achievers.students.mainFragments.HomeFragment;
import com.infiniterealm.achievers.students.mainFragments.HomeworkFragment;
import com.infiniterealm.achievers.students.mainFragments.ProfileFragment;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.students.mainFragments.ScheduleFragment;
import com.infiniterealm.achievers.students.mainFragments.TestsFragment;

public class StudentActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout layout;
    final int HOME = R.id.home;
    final int TESTS = R.id.tests;
    final int SCHEDULES = R.id.schedule;
    final int HOMEWORK = R.id.homework;
//    final int PROFILE = R.id.profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        navigateToFragment(new HomeFragment());
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        layout = findViewById(R.id.frame);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                navigateToFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.tests) {
                navigateToFragment(new TestsFragment());
            } else if (item.getItemId() == R.id.schedule) {
                navigateToFragment(new ScheduleFragment());
            } else if (item.getItemId() == R.id.homework) {
                navigateToFragment(new HomeworkFragment());
            } else if (item.getItemId() == R.id.profile) {
                navigateToFragment(new ProfileFragment());
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int activeID = bottomNavigationView.getSelectedItemId();
        if (activeID == HOME) {
            navigateToFragment(new HomeFragment());
        } else if (activeID == TESTS) {
            navigateToFragment(new TestsFragment());
        } else if (activeID == SCHEDULES) {
            navigateToFragment(new ScheduleFragment());
        } else if (activeID == HOMEWORK) {
            navigateToFragment(new HomeFragment());
        } else {
            navigateToFragment(new ProfileFragment());
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit the app?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // If user confirms exit, finish the activity
            finish();
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}