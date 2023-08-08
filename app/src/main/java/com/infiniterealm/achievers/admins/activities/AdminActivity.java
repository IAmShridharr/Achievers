package com.infiniterealm.achievers.admins.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.admins.mainFragments.AdminHomeFragment;
import com.infiniterealm.achievers.admins.mainFragments.AdminHomeworkFragment;
import com.infiniterealm.achievers.admins.mainFragments.AdminProfileFragment;
import com.infiniterealm.achievers.admins.mainFragments.AdminScheduleFragment;
import com.infiniterealm.achievers.admins.mainFragments.AdminTestsFragment;

public class AdminActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    final int HOME = R.id.admin_home;
    final int TESTS = R.id.admin_tests;
    final int SCHEDULE = R.id.admin_schedule;
    final int HOMEWORK = R.id.admin_homework;
    final int PROFILE = R.id.admin_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        navigateToFragment(new AdminHomeFragment());
        bottomNavigationView = findViewById(R.id.admin_bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == HOME) {
                navigateToFragment(new AdminHomeFragment());
            } else if (item.getItemId() == TESTS) {
                navigateToFragment(new AdminTestsFragment());
            } else if (item.getItemId() == SCHEDULE) {
                navigateToFragment(new AdminScheduleFragment());
            } else if (item.getItemId() == HOMEWORK) {
                navigateToFragment(new AdminHomeworkFragment());
            } else if (item.getItemId() == PROFILE) {
                navigateToFragment(new AdminProfileFragment());
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit the app?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // If user confirms exit, finish the activity
            finishAffinity();
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_frame, fragment);
        fragmentTransaction.commit();
    }
}