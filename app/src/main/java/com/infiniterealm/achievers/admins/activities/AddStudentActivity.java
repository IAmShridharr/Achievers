package com.infiniterealm.achievers.admins.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.admins.helper.StudentSignUpHelper;

import java.util.Objects;

public class AddStudentActivity extends AppCompatActivity {

    ConstraintLayout layout;
    EditText name, ID, email, password;
    Button add;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        layout = findViewById(R.id.add_student_layout);

        name = findViewById(R.id.input_name);
        ID = findViewById(R.id.input_id);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);

        add = findViewById(R.id.add);

        add.setOnClickListener(view -> {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("students");

            String Name = name.getText().toString();
            String iD = ID.getText().toString();
            String Email = email.getText().toString();
            String Password = password.getText().toString();
            final String[] UID = new String[1];

            if (Name.isEmpty() || iD.isEmpty() || Email.isEmpty() || Password.isEmpty()) {
                Snackbar snackbar = Snackbar.make(layout, "All fields are required!", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UID[0] = Objects.requireNonNull(task.getResult().getUser()).getUid();
                            StudentSignUpHelper studentSignUpHelper = new StudentSignUpHelper(Name, iD, Email, Password);
                            databaseReference.child(UID[0]).setValue(studentSignUpHelper);
                            databaseReference.child(UID[0]).child("profileImageUrl").setValue("");
                            databaseReference.child(UID[0]).child("phone").setValue("");
                            databaseReference.child(UID[0]).child("parentPhone").setValue("");
                            databaseReference.child(UID[0]).child("dob").setValue("");
                            databaseReference.child(UID[0]).child("school").setValue("");
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("roles");
                            ref.child("students").child(UID[0]).setValue(true);

                            name.setText(null);
                            ID.setText(null);
                            email.setText(null);
                            password.setText(null);

                            Snackbar snackbar = Snackbar
                                    .make(layout, "Student Added Successfully!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(layout, "Student Could Not Be Added!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
            }
        });
    }
}