package com.infiniterealm.achievers;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infiniterealm.achievers.admins.activities.AdminActivity;
import com.infiniterealm.achievers.explorer.activities.ExplorerActivity;
import com.infiniterealm.achievers.students.activities.StudentActivity;
import com.infiniterealm.achievers.utilities.SnackBarHelper;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDbRef;
    private SharedPreferences sharedPreferences;
    private View customSnackBarView;
    private TextView snackBarActionTurnOn, snackBarActionContinue;
    ConstraintLayout layout;
    private static final int SPLASH_SCREEN_TIMEOUT = 6000; // 6 seconds
    private static final int INTERNET_WAITING_TIME_WIFI = 10000; // 10 seconds
    private static final int INTERNET_WAITING_TIME_DATA = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        customSnackBarView = LayoutInflater.from(this).inflate(R.layout.custom_snackbar, layout, false);
        snackBarActionTurnOn = customSnackBarView.findViewById(R.id.action1);
        snackBarActionContinue = customSnackBarView.findViewById(R.id.action2);

        layout = findViewById(R.id.splash);

        mAuth = FirebaseAuth.getInstance();
        mDbRef = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        TextView textView = findViewById(R.id.textView);
        String Brand = "Achiever's Home";
        textView.setText(Brand);

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(2000); // 2 second
        animator.setRepeatCount(0);

        animator.addUpdateListener(animator1 -> {
            float value = (float) animator1.getAnimatedValue();
            textView.setAlpha(value);
        });

        animator.start();

        // Listeners for the SnackBar Action Buttons
        snackBarActionTurnOn.setOnClickListener(view -> showInternetOptionsDialog(sharedPreferences));
        snackBarActionContinue.setOnClickListener(view -> continueWithoutInternet(sharedPreferences));

        checkInternetConnection(sharedPreferences);
    }

    private void checkInternetConnection(SharedPreferences sharedPreferences) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            // Internet Connection Unavailable
            SnackBarHelper.showShortCustomSnackBar(this, layout, "No Internet", "Turn On", "Continue");
        } else {
            // Internet Connection Available
            new Handler().postDelayed(() -> {

                String savedEmail = sharedPreferences.getString("email", null);
                String savedPassword = sharedPreferences.getString("password", null);

                if (savedEmail != null && savedPassword != null) {
                    login(savedEmail, savedPassword);
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_SCREEN_TIMEOUT);
        }
    }

    private void showInternetOptionsDialog(SharedPreferences sharedPreferences) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
        builder.setTitle("Select Convenient Option");
        builder.setMessage("Please select an option to turn on internet");
        builder.setPositiveButton("TURN ON WIFI", (dialogInterface, i) -> {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
            new Handler().postDelayed(() -> checkInternetConnection(sharedPreferences), INTERNET_WAITING_TIME_WIFI);
        });
        builder.setNegativeButton("TURN ON DATA", (dialogInterface, i) -> {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
            startActivity(intent);
            new Handler().postDelayed(() -> checkInternetConnection(sharedPreferences), INTERNET_WAITING_TIME_DATA);
        });
        builder.show();
    }

    public void continueWithoutInternet(SharedPreferences sharedPreferences) {
        String savedEmail = sharedPreferences.getString("email", null);
        String savedPassword = sharedPreferences.getString("password", null);
        boolean isStudent = sharedPreferences.getBoolean("isStudent", false);

        Intent intent;
        if (savedEmail != null && savedPassword != null) {
            if (isStudent) {
                intent = new Intent(SplashScreenActivity.this, StudentActivity.class);
            } else {
                intent = new Intent(SplashScreenActivity.this, AdminActivity.class);
            }
        } else {
            intent = new Intent(SplashScreenActivity.this, ExplorerActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private void login(String savedEmail, String savedPassword) {
        mAuth.signInWithEmailAndPassword(savedEmail, savedPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                String uid = user.getUid();

                mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Check if uid exists in roles > admins
                        if (dataSnapshot.child("roles").child("admins").hasChild(uid)) {
                            Intent intent = new Intent(SplashScreenActivity.this, AdminActivity.class);
                            startActivity(intent);
                        }
                        // Check if uid exists in roles > students
                        else if (dataSnapshot.child("roles").child("students").hasChild(uid)) {
                            Intent intent = new Intent(SplashScreenActivity.this, StudentActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error case
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetConnection(sharedPreferences);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}