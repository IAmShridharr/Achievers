package com.infiniterealm.achievers.students.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.infiniterealm.achievers.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText name, rollNumber, email, dobInput, phone, parentPhone, school;
    private StorageReference profileImageRef;
    private String uid;
    private DatabaseReference mDbRef;
    FirebaseUser student;
    private TextView addDP, removeDP;
    private String DP, Name, RollNumber, Email, Phone, ParentPhone, DOB, School;
    private ShapeableImageView profileImage;
    private LinearLayout editProfileLayout;
    private ActivityResultLauncher<String> mGetContent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        progressBar = findViewById(R.id.progressBar);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        student = mAuth.getCurrentUser();
        assert student != null;
        uid = student.getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference("students").child(uid);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        profileImageRef = storage.getReference().child("students/profile_pictures/" + uid);

        profileImage = findViewById(R.id.edit_profile_image);

        addDP = findViewById(R.id.add_profile_picture);
        removeDP = findViewById(R.id.remove_profile_picture);
        name = findViewById(R.id.input_name);
        rollNumber = findViewById(R.id.input_id);
        email = findViewById(R.id.input_email);
        dobInput = findViewById(R.id.input_dob);
        phone = findViewById(R.id.input_phone);
        parentPhone = findViewById(R.id.input_parent_phone);
        school = findViewById(R.id.input_school);

        Button update = findViewById(R.id.update);

        TextInputLayout dobInputLayout = findViewById(R.id.input_dob_layout);
        editProfileLayout = findViewById(R.id.edit_profile_form_layout);

        showData();

        dobInputLayout.setStartIconOnClickListener(view -> {
            // Create a MaterialDatePicker instance
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Select Date of Birth");
            MaterialDatePicker<Long> datePicker = builder.build();

            // Set the selected date on the text field
            datePicker.addOnPositiveButtonClickListener(selection -> {
                // Format the selected date to the desired string
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String selectedDate = dateFormat.format(new Date(selection));

                // Set the selected date on the text field
                dobInput.setText(selectedDate);
            });

            // Show the date picker dialog
            datePicker.show(getSupportFragmentManager(), "datePicker");
        });

        update.setOnClickListener(view -> {
            boolean isPhoneChanged = false, isParentPhoneChanged = false, isDOBChanged = false, isSchoolChanged = false;
            if (isPhoneChanged()) {
                isPhoneChanged = true;
            }
            if (isParentPhoneChanged()) {
                isParentPhoneChanged = true;
            }
            if (isDOBChanged()) {
                isDOBChanged = true;
            }
            if (isSchoolChanged()) {
                isSchoolChanged = true;
            }
            Snackbar snackbar;
            if (isPhoneChanged || isParentPhoneChanged || isDOBChanged || isSchoolChanged) {
                snackbar = Snackbar.make(editProfileLayout, "Profile Updated Successfully!", Snackbar.LENGTH_LONG);
            } else {
                snackbar = Snackbar.make(editProfileLayout, "No Changes Found!", Snackbar.LENGTH_LONG);
            }
            snackbar.show();
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            // Handle the selected image URI here
            progressBar.setVisibility(View.VISIBLE);

            StorageReference setImage = profileImageRef.child(uid + ".jpg");

            UploadTask uploadTask = setImage.putFile(result);
            if (uploadTask.isComplete()) {
                Snackbar snackbar = Snackbar.make(editProfileLayout, "Profile Picture Uploaded!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            uploadTask.addOnSuccessListener(taskSnapshot -> {

                // Get the download URL of the uploaded image
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    // Update the user's database with the download URL
                    String downloadUrl = uri.toString();
                    mDbRef.child("profileImageUrl").setValue(downloadUrl);

                    mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.hasChild("profileImageUrl")) {
                                String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                                // Use the profileImageUrl to load the image using Glide
                                Glide.with(getApplicationContext())
                                        .load(profileImageUrl)
                                        .into(profileImage);
                                Snackbar snackbar = Snackbar.make(editProfileLayout, "Profile Picture Updated Successfully!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                removeDP.setVisibility(View.VISIBLE);
                                addDP.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(editProfileLayout, "Something went wrong!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                });
            }).addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(editProfileLayout, "Something went wrong!", Snackbar.LENGTH_LONG);
                snackbar.show();
            });
        });
    }
        public void showData() {
        progressBar.setVisibility(View.VISIBLE);
        mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DP = snapshot.child("profileImageUrl").getValue(String.class);
                Name = snapshot.child("name").getValue(String.class);
                RollNumber = snapshot.child("id").getValue(String.class);
                Email = snapshot.child("email").getValue(String.class);
                Phone = snapshot.child("phone").getValue(String.class);
                ParentPhone = snapshot.child("parentPhone").getValue(String.class);
                DOB = snapshot.child("dob").getValue(String.class);
                School = snapshot.child("school").getValue(String.class);

                if (DP == null || DP.equals("")) {
                    profileImage.setImageResource(R.drawable.profile_picture_placeholder);
                    removeDP.setVisibility(View.GONE);
                    addDP.setVisibility(View.VISIBLE);
                } else {
                    Glide.with(getApplicationContext())
                            .load(DP)
                            .into(profileImage);
                    removeDP.setVisibility(View.VISIBLE);
                    addDP.setVisibility(View.GONE);
                }
                assert Name != null;
                if (Name.isEmpty()) {
                    name.setText(Name);
                } else {
                    name.setText(Name);
                }
                assert RollNumber != null;
                if (RollNumber.isEmpty()) {
                    rollNumber.setText(RollNumber);
                } else {
                    rollNumber.setText(RollNumber);
                }
                assert Email != null;
                if (Email.isEmpty()) {
                    email.setText(Email);
                } else {
                    email.setText(Email);
                }
                assert Phone != null;
                if (Phone.isEmpty()) {
                    phone.setText(Phone);
                } else {
                    phone.setText(Phone);
                }
                assert ParentPhone != null;
                if (ParentPhone.isEmpty()) {
                    parentPhone.setText(ParentPhone);
                } else {
                    parentPhone.setText(ParentPhone);
                }
                assert DOB != null;
                if (DOB.isEmpty()) {
                    dobInput.setText(DOB);
                } else {
                    dobInput.setText(DOB);
                }
                assert School != null;
                if (School.isEmpty()) {
                    school.setText(School);
                } else {
                    school.setText(School);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    public boolean isPhoneChanged() {
        if (!Phone.equals(Objects.requireNonNull(phone.getText()).toString())) {
            mDbRef.child("phone").setValue(phone.getText().toString());
            Phone = phone.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isParentPhoneChanged() {
        if (!ParentPhone.equals(Objects.requireNonNull(parentPhone.getText()).toString())) {
            mDbRef.child("parentPhone").setValue(parentPhone.getText().toString());
            ParentPhone = parentPhone.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isDOBChanged() {
        if (!DOB.equals(Objects.requireNonNull(dobInput.getText()).toString())) {
            if (dobInput.getText().toString().length() == 10 || dobInput.getText().toString().equals("")) {
                mDbRef.child("dob").setValue(dobInput.getText().toString());
                DOB = dobInput.getText().toString();
                return true;
            }
        }
        return false;
    }

    public boolean isSchoolChanged() {
        if (!School.equals(Objects.requireNonNull(school.getText()).toString())) {
            mDbRef.child("school").setValue(school.getText().toString());
            School = school.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public void selectImage(View view) {
        // Launch the intent to select an image from the device's gallery
        mGetContent.launch("image/*");
    }

    public void deleteImage(View view) {
        // Remove the user's profile picture from Firebase Storage
        progressBar.setVisibility(View.VISIBLE);
        profileImageRef.child(uid + ".jpg").delete().addOnSuccessListener(aVoid -> {
            // Remove the profile picture URL from the user's Firebase Realtime Database entry
            mDbRef.child("profileImageUrl").setValue("").addOnSuccessListener(aVoid1 -> {
                // Set the default profile picture in the ImageView
                profileImage.setImageResource(R.drawable.profile_picture_placeholder);
                progressBar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(editProfileLayout, "Profile Picture Deleted Successfully!", Snackbar.LENGTH_LONG);
                snackbar.show();
                removeDP.setVisibility(View.GONE);
                addDP.setVisibility(View.VISIBLE);
            }).addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(editProfileLayout, "Something went wrong!", Snackbar.LENGTH_LONG);
                snackbar.show();
            });
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar.make(editProfileLayout, "No Profile Picture Exist!", Snackbar.LENGTH_LONG);
            snackbar.show();
        });
    }
}