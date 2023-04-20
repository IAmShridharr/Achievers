package com.infiniterealm.achievers.admins.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.admins.fragments.AdminHomeFragment;
import com.infiniterealm.achievers.admins.fragments.AdminHomeworkFragment;
import com.infiniterealm.achievers.admins.fragments.AdminProfileFragment;
import com.infiniterealm.achievers.admins.fragments.AdminScheduleFragment;
import com.infiniterealm.achievers.admins.fragments.AdminTestsFragment;

public class AdminActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        navigateToFragment(new AdminHomeFragment());
        bottomNavigationView = findViewById(R.id.admin_bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            final int HOME = R.id.admin_home;
            final int TESTS = R.id.admin_tests;
            final int SCHEDULES = R.id.admin_schedule;
            final int HOMEWORK = R.id.admin_homework;
            final int PROFILE = R.id.admin_profile;
            switch (item.getItemId()) {
                case HOME:
                    navigateToFragment(new AdminHomeFragment());
                    break;
                case TESTS:
                    navigateToFragment(new AdminTestsFragment());
                    break;
                case SCHEDULES:
                    navigateToFragment(new AdminScheduleFragment());
                    break;
                case HOMEWORK:
                    navigateToFragment(new AdminHomeworkFragment());
                    break;
                case PROFILE:
                    navigateToFragment(new AdminProfileFragment());
                    break;
            }
            return true;
        });
    }
    private void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_frame, fragment);
        fragmentTransaction.commit();
    }
}