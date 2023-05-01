package com.infiniterealm.achievers.explorer.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.explorer.mainFragments.ExplorerContactFragment;
import com.infiniterealm.achievers.explorer.mainFragments.ExplorerExploreFragment;
import com.infiniterealm.achievers.explorer.mainFragments.ExplorerHomeFragment;

public class ExplorerActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    final int HOME = R.id.explorer_home;
    final int EXPLORE = R.id.explorer_explore;
    final int CONTACT = R.id.explorer_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        navigateToFragment(new ExplorerExploreFragment());
        bottomNavigationView = findViewById(R.id.explorer_bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == HOME) {
                navigateToFragment(new ExplorerHomeFragment());
            } else if (item.getItemId() == EXPLORE) {
                navigateToFragment(new ExplorerExploreFragment());
            } else if (item.getItemId() == CONTACT) {
                navigateToFragment(new ExplorerContactFragment());
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int activeID = bottomNavigationView.getSelectedItemId();
        if (activeID == HOME) {
            navigateToFragment(new ExplorerHomeFragment());
        } else if (activeID == EXPLORE) {
            navigateToFragment(new ExplorerExploreFragment());
        } else {
            navigateToFragment(new ExplorerContactFragment());
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
        fragmentTransaction.replace(R.id.explorer_frame, fragment);
        fragmentTransaction.commit();
    }
}