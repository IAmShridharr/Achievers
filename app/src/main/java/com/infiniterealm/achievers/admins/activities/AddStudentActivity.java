package com.infiniterealm.achievers.admins.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
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
            String std = iD.substring(0, 1);
            String standard;
            switch (std) {
                case "J":
                    standard = "Jr. KG";
                    break;
                case "S":
                    standard = "Sr. KG";
                    break;
                case "1":
                    standard = "1st Standard";
                    break;
                case "2":
                    standard = "2nd Standard";
                    break;
                case "3":
                    standard = "3rd Standard";
                    break;
                case "4":
                    standard = "4th Standard";
                    break;
                case "5":
                    standard = "5th Standard";
                    break;
                case "6":
                    standard = "6th Standard";
                    break;
                case "7":
                    standard = "7th Standard";
                    break;
                case "8":
                    standard = "8th Standard";
                    break;
                case "9":
                    standard = "9th Standard";
                    break;
                case "X":
                    standard = "10th Standard";
                    break;
                default:
                    standard = "Pre-School";
                    break;
            }
            String Email = email.getText().toString();
            String Password = password.getText().toString();
            final String[] UID = new String[1];

            if (Name.isEmpty() || iD.isEmpty() || Email.isEmpty() || Password.isEmpty()) {
                showSnackBar(view, "All fields are required!");
            } else {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        UID[0] = Objects.requireNonNull(task.getResult().getUser()).getUid();
                        StudentSignUpHelper studentSignUpHelper = new StudentSignUpHelper(Name, iD, Email, Password);
                        databaseReference.child(standard).child(iD).setValue(studentSignUpHelper);
                        databaseReference.child(standard).child(iD).child("UID").setValue(UID[0]);
                        databaseReference.child(standard).child(iD).child("profileImageUrl").setValue("");
                        databaseReference.child(standard).child(iD).child("phone").setValue("");
                        databaseReference.child(standard).child(iD).child("parentPhone").setValue("");
                        databaseReference.child(standard).child(iD).child("DOB").setValue("");
                        databaseReference.child(standard).child(iD).child("school").setValue("");
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("roles");
                        ref.child("students").child(UID[0]).setValue(true);

                        name.setText(null);
                        ID.setText(null);
                        email.setText(null);
                        password.setText(null);

                        showSnackBar(view, "Student Added Successfully!");
                    } else {
                        showSnackBar(view, "Student Could Not Be Added!");
                    }
                });
            }
        });
    }

    private void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}