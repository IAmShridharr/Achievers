package com.infiniterealm.achievers;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.infiniterealm.achievers.admins.activities.AdminActivity;
import com.infiniterealm.achievers.explorer.activities.ExplorerActivity;
import com.infiniterealm.achievers.students.activities.StudentActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ConstraintLayout layout;
    TextInputEditText loginID, Password;
    ProgressBar progressBar;
    Button login;
    TextView skip, forgetPassword;

    SharedPreferences mPrefs;

    private FirebaseAuth mAuth;
    private DatabaseReference mDbRef;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layout = findViewById(R.id.login_layout);
        loginID = findViewById(R.id.input_login_id);
        Password = findViewById(R.id.input_password);
        progressBar = findViewById(R.id.progress);
        login = findViewById(R.id.login);
        skip = findViewById(R.id.skip);
        forgetPassword = findViewById(R.id.forget_password);

        mPrefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        skip.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ExplorerActivity.class);
            startActivity(intent);
            finish();
        });

        login.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if (!Objects.requireNonNull(loginID.getText()).toString().isEmpty() && !Objects.requireNonNull(Password.getText()).toString().isEmpty()) {
                if (loginID.getText().toString().contains("A")) {
                    AdminLogin();
                } else {
                    StudentLogin();
                }
            } else {
                progressBar.setVisibility(View.GONE);

                showSnackBar(layout, "All Fields Are Required");
            }
        });

        forgetPassword.setOnClickListener(view -> {
            String email = Objects.requireNonNull(loginID.getText()).toString().trim();

            if (TextUtils.isEmpty(email)) {
                showSnackBar(layout, "Please enter your email");
                return;
            }

            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showSnackBar(layout, "Password reset email has been sent to your email");
                } else {
                    showSnackBar(layout, "Failed to send reset email. Please try again");
                }
            });
        });
    }

    public void StudentLogin() {
        String rollNumber = Objects.requireNonNull(loginID.getText()).toString();
        String password = Objects.requireNonNull(Password.getText()).toString();

        reference = FirebaseDatabase.getInstance().getReference().child("Auth");
        Query query = reference.orderByChild("id").equalTo(rollNumber);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loginID.setError(null);
                    DataSnapshot firstChild = snapshot.getChildren().iterator().next();

                    reference.child(Objects.requireNonNull(firstChild.getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String passwordFromDB = snapshot.child("password").getValue(String.class);
                            if (password.equals(passwordFromDB)) {
                                Password.setError(null);
                                String email = snapshot.child("email").getValue(String.class);

                                mAuth = FirebaseAuth.getInstance();
                                mDbRef = FirebaseDatabase.getInstance().getReference();

                                assert email != null;
                                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        // Sign in success, update UI with the signed-in user's information
                                        mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                // Save the user's login status
                                                SharedPreferences.Editor editor = mPrefs.edit();
                                                editor.putBoolean("isLoggedIn", true);
                                                editor.putBoolean("isStudent", true);
                                                editor.putString("email", email);
                                                editor.putString("password", password);
                                                editor.putString("id", rollNumber);
                                                editor.apply();

                                                Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Handle error case
                                                // display an error message to the user
                                                showSnackBar(layout, "Error retrieving data from database");
                                            }
                                        });
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        // If sign in fails, display a message to the user.
                                        if (Objects.requireNonNull(task.getException()).toString().contains("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException")) {
                                            showSnackBar(layout, Objects.requireNonNull(task.getException()).toString().substring(66));
                                        } else if (Objects.requireNonNull(task.getException()).toString().contains("com.google.firebase.auth.FirebaseAuthInvalidUserException")) {
                                            showSnackBar(layout, Objects.requireNonNull(task.getException()).toString().substring(59));
                                        } else if (Objects.requireNonNull(task.getException()).toString().contains("com.google.firebase.FirebaseTooManyRequestsException")) {
                                            showSnackBar(layout, Objects.requireNonNull(task.getException()).toString().substring(54, 141));
                                        } else {
                                            showSnackBar(layout, "Something went wrong!");
                                        }
                                    }
                                });

                            } else {
                                Password.setError("Invalid Password");
                                Password.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    loginID.setError("Invalid Login ID");
                    loginID.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void AdminLogin() {
        String rollNumber = Objects.requireNonNull(loginID.getText()).toString();
        String password = Objects.requireNonNull(Password.getText()).toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("admins");
        Query query = reference.orderByChild("id").equalTo(rollNumber);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loginID.setError(null);
                    DataSnapshot firstChild = snapshot.getChildren().iterator().next();
                    String passwordFromDB = firstChild.child("password").getValue(String.class);
                    assert passwordFromDB != null;
                    Log.d(TAG, passwordFromDB);
                    if (password.equals(passwordFromDB)) {
                        Password.setError(null);
                        String email = firstChild.child("email").getValue(String.class);

                        mAuth = FirebaseAuth.getInstance();
                        mDbRef = FirebaseDatabase.getInstance().getReference();

                        assert email != null;
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                // Sign in success, update UI with the signed-in user's information
                                mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // Save the user's login status
                                        SharedPreferences.Editor editor = mPrefs.edit();
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.putBoolean("isAdmin", true);
                                        editor.putString("id", rollNumber);
                                        editor.putString("email", email);
                                        editor.putString("password", password);
                                        editor.apply();

                                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle error case
                                        // display an error message to the user
                                        Snackbar snackbar = Snackbar
                                                .make(layout, "Error retrieving data from database", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                });
                            } else {
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user.
                                if (Objects.requireNonNull(task.getException()).toString().contains("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException")) {
                                    showSnackBar(layout, Objects.requireNonNull(task.getException()).toString().substring(66));
                                } else if (Objects.requireNonNull(task.getException()).toString().contains("com.google.firebase.auth.FirebaseAuthInvalidUserException")) {
                                    showSnackBar(layout, Objects.requireNonNull(task.getException()).toString().substring(59));
                                } else if (Objects.requireNonNull(task.getException()).toString().contains("com.google.firebase.FirebaseTooManyRequestsException")) {
                                    showSnackBar(layout, Objects.requireNonNull(task.getException()).toString().substring(54, 141));
                                } else {
                                    showSnackBar(layout, "Something went wrong!");
                                }
                            }
                        });
                    } else {
                        Password.setError("Invalid Password");
                        Password.requestFocus();
                    }
                } else {
                    loginID.setError("Invalid Login ID");
                    loginID.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}