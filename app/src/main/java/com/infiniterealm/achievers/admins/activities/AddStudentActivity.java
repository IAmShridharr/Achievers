package com.infiniterealm.achievers.admins.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
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
    MaterialButton add;
    RadioGroup role;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        layout = findViewById(R.id.add_student_layout);

        name = findViewById(R.id.input_name);
        ID = findViewById(R.id.input_id);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        role = findViewById(R.id.role);

        add = findViewById(R.id.add);

        add.setOnClickListener(view -> {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("students");
            rootRef = firebaseDatabase.getReference();

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
            int selectedRole = role.getCheckedRadioButtonId();

            String Role;
            if (selectedRole == -1) {
                showSnackBar(view, "Please Select Role!");
                return;
            } else {
                MaterialRadioButton selectedRadioBtn = findViewById(selectedRole);
                if (selectedRadioBtn.getText().toString().equals("Student")) {
                    Role = "Student";
                } else if (selectedRadioBtn.getText().toString().equals("Admin")) {
                    Role = "Admin";
                } else {
                    Role = null;
                }
            }
            final String[] UID = new String[1];

            if (Name.isEmpty() || iD.isEmpty() || Email.isEmpty() || Password.isEmpty()) {
                showSnackBar(view, "All fields are required!");
            } else {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        UID[0] = Objects.requireNonNull(task.getResult().getUser()).getUid();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("roles");
                        assert Role != null;
                        if (Role.equals("Student")) {
                            ref.child("students").child(UID[0]).setValue(true);
                            StudentSignUpHelper studentSignUpHelper = new StudentSignUpHelper(Name, iD, Email, Password, UID[0], Role);
                            rootRef.child("Auth").child(iD).setValue(studentSignUpHelper);
                            databaseReference.child(standard).child(iD).child("Profile Information").child("name").setValue(Name);
                            databaseReference.child(standard).child(iD).child("Profile Information").child("id").setValue(iD);
                            databaseReference.child(standard).child(iD).child("Profile Information").child("email").setValue(Email);
                            databaseReference.child(standard).child(iD).child("Profile Information").child("profileImageURL").setValue("");
                            databaseReference.child(standard).child(iD).child("Profile Information").child("phone").setValue("");
                            databaseReference.child(standard).child(iD).child("Profile Information").child("parentPhone").setValue("");
                            databaseReference.child(standard).child(iD).child("Profile Information").child("DOB").setValue("");
                            databaseReference.child(standard).child(iD).child("Profile Information").child("school").setValue("");
                            databaseReference.child(standard).child(iD).child("Profile Information").child("followers").setValue(0);
                            databaseReference.child(standard).child(iD).child("Profile Information").child("tests").setValue(0);
                            databaseReference.child(standard).child(iD).child("Profile Information").child("followings").setValue(0);
                        } else {
                            ref.child("admins").child(UID[0]).setValue(true);
                        }

                        name.setText(null);
                        ID.setText(null);
                        email.setText(null);
                        password.setText(null);
                        role.clearCheck();

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