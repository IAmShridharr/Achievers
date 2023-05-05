package com.infiniterealm.achievers.students.mainFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.students.activities.EditProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    View rootView;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference mDbRef;
    String uid;
    TextView name, standard, rollNumber, email, phone, parentPhone, dob, school;
    ProgressBar progressBar;
    LinearLayout rollNumberLayout, classLayout, emailLayout, phoneLayout, parentPhoneLayout, dobLayout, schoolLayout, profileLayout;
    private ShapeableImageView profileImage;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        name = rootView.findViewById(R.id.name);
        standard = rootView.findViewById(R.id.standard);
        rollNumber = rootView.findViewById(R.id.roll_number);
        email = rootView.findViewById(R.id.email);
        phone = rootView.findViewById(R.id.phone);
        parentPhone = rootView.findViewById(R.id.parent_phone);
        dob = rootView.findViewById(R.id.dob);
        school = rootView.findViewById(R.id.school);

        profileImage = rootView.findViewById(R.id.profile_image);

        profileLayout = rootView.findViewById(R.id.profile_layout);
        classLayout = rootView.findViewById(R.id.class_layout);
        emailLayout = rootView.findViewById(R.id.email_layout);
        rollNumberLayout = rootView.findViewById(R.id.roll_number_layout);
        phoneLayout = rootView.findViewById(R.id.phone_layout);
        parentPhoneLayout = rootView.findViewById(R.id.parent_phone_layout);
        dobLayout = rootView.findViewById(R.id.dob_layout);
        schoolLayout = rootView.findViewById(R.id.school_layout);

        mDbRef = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        uid = user.getUid();

        if (!uid.isEmpty()) {
            getStudentData();
        }

        rootView.findViewById(R.id.edit_profile).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        rootView.findViewById(R.id.contact_teacher).setOnClickListener(view -> {

        });

        return rootView;

    }

    private void getStudentData() {
        mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String DP = snapshot.child(uid).child("profileImageUrl").getValue(String.class);
                String Name = snapshot.child(uid).child("name").getValue(String.class);
                String Standard = snapshot.child(uid).child("class").getValue(String.class);
                String Email = snapshot.child(uid).child("email").getValue(String.class);
                String RollNumber = snapshot.child(uid).child("roll_no").getValue(String.class);
                String Phone = snapshot.child(uid).child("phone").getValue(String.class);
                String ParentPhone = snapshot.child(uid).child("parent_phone").getValue(String.class);
                String DOB = snapshot.child(uid).child("dob").getValue(String.class);
                String School = snapshot.child(uid).child("school").getValue(String.class);

                if (DP == null) {
                    profileImage.setImageResource(R.drawable.profile_picture_placeholder);
                } else {
                    if (isAdded()) {
                        Glide.with(requireContext())
                                .load(DP)
                                .into(profileImage);
                    }
                }
                assert Name != null;
                if (Name.isEmpty()) {
                    String nameText = "Set Name";
                    name.setText(nameText);
                } else {
                    name.setText(Name);
                }
                assert Standard != null;
                if (Standard.isEmpty()) {
                    classLayout.setVisibility(View.GONE);
                } else {
                    classLayout.setVisibility(View.VISIBLE);
                    standard.setText(Standard);
                }
                assert RollNumber != null;
                if (RollNumber.isEmpty()) {
                    rollNumberLayout.setVisibility(View.GONE);
                } else {
                    rollNumberLayout.setVisibility(View.VISIBLE);
                    rollNumber.setText(RollNumber);
                }
                assert Email != null;
                if (Email.isEmpty()) {
                    emailLayout.setVisibility(View.GONE);
                } else {
                    emailLayout.setVisibility(View.VISIBLE);
                    email.setText(Email);
                }
                assert Phone != null;
                if (Phone.isEmpty()) {
                    phoneLayout.setVisibility(View.GONE);
                } else {
                    phoneLayout.setVisibility(View.VISIBLE);
                    phone.setText(Phone);
                }
                assert ParentPhone != null;
                if (ParentPhone.isEmpty()) {
                    parentPhoneLayout.setVisibility(View.GONE);
                } else {
                    parentPhoneLayout.setVisibility(View.VISIBLE);
                    parentPhone.setText(ParentPhone);
                }
                assert DOB != null;
                if (DOB.isEmpty()) {
                    dobLayout.setVisibility(View.GONE);
                } else {
                    dobLayout.setVisibility(View.VISIBLE);
                    dob.setText(DOB);
                }
                assert School != null;
                if (School.isEmpty()) {
                    schoolLayout.setVisibility(View.GONE);
                } else {
                    schoolLayout.setVisibility(View.VISIBLE);
                    school.setText(School);
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar = Snackbar.make(profileLayout, "Something went wrong", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }
}