package com.infiniterealm.achievers.students.activities;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.network.NetworkUtils;
import com.infiniterealm.achievers.students.mainFragments.HomeFragment;
import com.infiniterealm.achievers.students.mainFragments.HomeworkFragment;
import com.infiniterealm.achievers.students.mainFragments.ProfileFragment;
import com.infiniterealm.achievers.students.mainFragments.ScheduleFragment;
import com.infiniterealm.achievers.students.mainFragments.TestsFragment;
import com.infiniterealm.achievers.utilities.Essentials;
import com.infiniterealm.achievers.utilities.SnackBarHelper;

public class StudentActivity extends AppCompatActivity {

    ConstraintLayout layout;
    private NetworkUtils networkUtils;
    BottomNavigationView bottomNavigationView;
    final int HOME = R.id.homePage;
    final int TESTS = R.id.testsPage;
    final int SCHEDULE = R.id.schedulePage;
    final int HOMEWORK = R.id.homeworkPage;
    final int PROFILE = R.id.profilePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        layout = findViewById(R.id.studentActivityLayout);

        networkUtils = new NetworkUtils(this);

        navigateToFragment(new HomeFragment());
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == HOME) {
                navigateToFragment(new HomeFragment());
            } else if (item.getItemId() == TESTS) {
                navigateToFragment(new TestsFragment());
            } else if (item.getItemId() == SCHEDULE) {
                navigateToFragment(new ScheduleFragment());
            } else if (item.getItemId() == HOMEWORK) {
                navigateToFragment(new HomeworkFragment());
            } else if (item.getItemId() == PROFILE) {
                navigateToFragment(new ProfileFragment());
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

    @Override
    protected void onResume() {
        super.onResume();
        if(Essentials.checkNetwork(networkUtils)) {
            SnackBarHelper.showShortSnackBar(layout, "No Internet!");
        }

        int activeID = bottomNavigationView.getSelectedItemId();
        if (activeID == HOME) {
            navigateToFragment(new HomeFragment());
        } else if (activeID == TESTS) {
            navigateToFragment(new TestsFragment());
        } else if (activeID == SCHEDULE) {
            navigateToFragment(new ScheduleFragment());
        } else if (activeID == HOMEWORK) {
            navigateToFragment(new HomeFragment());
        } else {
            navigateToFragment(new ProfileFragment());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the network callback to avoid leaks
        networkUtils.unregisterNetworkCallback();
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}