package com.infiniterealm.achievers.students.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.network.NetworkUtils;
import com.infiniterealm.achievers.utilities.SnackBarHelper;
import com.infiniterealm.achievers.students.mainFragments.HomeFragment;
import com.infiniterealm.achievers.students.mainFragments.HomeworkFragment;
import com.infiniterealm.achievers.students.mainFragments.ProfileFragment;
import com.infiniterealm.achievers.students.mainFragments.ScheduleFragment;
import com.infiniterealm.achievers.students.mainFragments.TestsFragment;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

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

        ConstraintLayout layout = findViewById(R.id.studentActivityLayout);

        networkUtils = new NetworkUtils(this);

        navigateToFragment(new HomeFragment());
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Check network status and handle the refresh action when internet is available
        networkUtils.checkNetworkStatus(new NetworkUtils.OnNetworkChangeListener() {
            @Override
            public void onNetworkAvailable() {
                // Internet is available, trigger refresh or any action
                SnackBarHelper.showShortSnackBarWithAnchorView(layout, "You are Connected", bottomNavigationView);
            }

            @Override
            public void onNetworkUnavailable() {
                // Internet is unavailable, handle this case if needed
                Snackbar snackbar = Snackbar.make(layout, "No Internet", Snackbar.LENGTH_INDEFINITE).setAction("Refresh", view -> refresh());
                snackbar.show();
            }
        });

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

    public void refresh() {
        // Restart the activity
        Intent intent = getIntent();
        if (isIntentSafe(intent)) {
            finish();
            startActivity(intent);
        } else {
            // Handle the case when the intent is not safe (e.g., show an error message)
        }
    }

    private boolean isIntentSafe(Intent intent) {
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = intent.resolveActivity(packageManager);
        if (componentName != null) {
            String packageName = componentName.getPackageName();
            ArrayList<String> allowedPackages = getAllowedPackages(); // Add your allowed package names here
            return allowedPackages.contains(packageName);
        }
        return false;
    }

    private ArrayList<String> getAllowedPackages() {
        ArrayList<String> allowedPackages = new ArrayList<>();
        allowedPackages.add("com.infiniterealm.achievers"); // Add the package name of your trusted app here
        // Add more allowed packages if needed
        return allowedPackages;
    }

    @Override
    protected void onResume() {
        super.onResume();
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