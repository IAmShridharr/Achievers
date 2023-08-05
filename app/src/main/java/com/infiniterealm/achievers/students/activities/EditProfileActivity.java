package com.infiniterealm.achievers.students.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.students.helper.StudentRepository;
import com.infiniterealm.achievers.students.models.StudentProfileModel;
import com.infiniterealm.achievers.utilities.Essentials;
import com.infiniterealm.achievers.utilities.SnackBarHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String ID;
    String standard;
    MaterialButton update;
    TextInputLayout dobInputLayout;
    private TextInputEditText name, rollNumber, email, dobInput, phone, parentPhone, school;
    private StorageReference profileImageRef;
    private DatabaseReference mDbRef;
    private TextView addDP, removeDP;
    private String Phone, ParentPhone, DOB, School;
    private ShapeableImageView profileImage;
    private LinearLayout editProfileLayout;
    private ActivityResultLauncher<String> mGetContent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        defineLayout();

        sharedPreferences = getApplicationContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        ID = sharedPreferences.getString("id", null);
        assert ID != null;
        standard = Essentials.getStandard(ID);

        mDbRef = FirebaseDatabase.getInstance().getReference("students").child(standard).child(ID).child("Profile Information");

        FirebaseStorage storage = FirebaseStorage.getInstance();

        String path = "students/" + standard + "/" + ID + "/profile_picture/";
        profileImageRef = storage.getReference().child(path);

        getAndDisplayStudentData(ID);

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
            if (isPhoneChanged || isParentPhoneChanged || isDOBChanged || isSchoolChanged) {
                SnackBarHelper.showShortSnackBar(editProfileLayout, "Profile Updated Successfully!");
            } else {
                SnackBarHelper.showShortSnackBar(editProfileLayout, "No Changes Found!");
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            // Handle the selected image URI here
            progressBar.setVisibility(View.VISIBLE);

            StorageReference setImage = profileImageRef.child(ID + ".jpg");

            UploadTask uploadTask = setImage.putFile(result);
            if (uploadTask.isComplete()) {
                SnackBarHelper.showShortSnackBar(editProfileLayout, "Profile Picture Uploaded!");
            }
            uploadTask.addOnSuccessListener(taskSnapshot -> {

                // Get the download URL of the uploaded image
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    // Update the user's database with the download URL
                    String downloadUrl = uri.toString();
                    mDbRef.child("profileImageURL").setValue(downloadUrl);

                    mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.hasChild("profileImageURL")) {
                                String profileImageUrl = dataSnapshot.child("profileImageURL").getValue(String.class);
                                // Use the profileImageUrl to load the image using Glide
                                Glide.with(getApplicationContext())
                                        .load(profileImageUrl)
                                        .into(profileImage);
                                SnackBarHelper.showShortSnackBar(editProfileLayout, "Profile Picture Updated Successfully!");
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
                    SnackBarHelper.showShortSnackBar(editProfileLayout, "Something went wrong!");
                });
            }).addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                SnackBarHelper.showShortSnackBar(editProfileLayout, "Something went wrong!");
            });
        });
    }

    private void getAndDisplayStudentData(String studentID) {
        StudentRepository studentRepository = new StudentRepository();
        studentRepository.getStudentData(studentID, new StudentRepository.OnStudentDataListener() {
            @Override
            public void onStudentDataLoaded(StudentProfileModel student) {
                // Update the UI with the fetched data

                if (student.getProfileImageURL() == null || student.getProfileImageURL().equals("")) {
                    profileImage.setImageResource(R.drawable.profile_picture_placeholder);
                    addDP.setVisibility(View.VISIBLE);
                    removeDP.setVisibility(View.GONE);
                } else {
                    Glide.with(getApplicationContext())
                            .load(student.getProfileImageURL())
                            .into(profileImage);
                    addDP.setVisibility(View.GONE);
                    removeDP.setVisibility(View.VISIBLE);
                }

                name.setText(student.getName());
                rollNumber.setText(student.getId());
                email.setText(student.getEmail());
                dobInput.setText(student.getDOB());
                phone.setText(student.getPhone());
                parentPhone.setText(student.getParentPhone());
                school.setText(student.getSchool());

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onStudentDataError(String errorMessage) {
                // Handle error
                SnackBarHelper.showShortSnackBar(editProfileLayout, errorMessage);
            }
        });
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
                mDbRef.child("DOB").setValue(dobInput.getText().toString());
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
        profileImageRef.child(ID + ".jpg").delete().addOnSuccessListener(aVoid -> {
            // Remove the profile picture URL from the user's Firebase Realtime Database entry
            mDbRef.child("profileImageURL").setValue("").addOnSuccessListener(aVoid1 -> {
                // Set the default profile picture in the ImageView
                profileImage.setImageResource(R.drawable.profile_picture_placeholder);
                progressBar.setVisibility(View.GONE);
                SnackBarHelper.showShortSnackBar(editProfileLayout, "Profile Picture Deleted Successfully!");
                removeDP.setVisibility(View.GONE);
                addDP.setVisibility(View.VISIBLE);
            }).addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                SnackBarHelper.showShortSnackBar(editProfileLayout, "Something went wrong!");
            });
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            SnackBarHelper.showShortSnackBar(editProfileLayout, "No Profile Picture Exists!");
        });
    }

    private void defineLayout() {
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

        update = findViewById(R.id.update);

        dobInputLayout = findViewById(R.id.input_dob_layout);
        editProfileLayout = findViewById(R.id.edit_profile_form_layout);

        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}