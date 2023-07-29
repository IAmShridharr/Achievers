package com.infiniterealm.achievers.students.mainFragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import java.net.URLEncoder;

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
    TextView name, rollNumber, email, phone, parentPhone, dob, school;
    ProgressBar progressBar;
    LinearLayout rollNumberLayout, emailLayout, phoneLayout, parentPhoneLayout, dobLayout, schoolLayout, profileLayout;
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
        rollNumber = rootView.findViewById(R.id.roll_number);
        email = rootView.findViewById(R.id.email);
        phone = rootView.findViewById(R.id.phone);
        parentPhone = rootView.findViewById(R.id.parent_phone);
        dob = rootView.findViewById(R.id.dob);
        school = rootView.findViewById(R.id.school);

        profileImage = rootView.findViewById(R.id.profile_image);

        profileLayout = rootView.findViewById(R.id.profile_layout);
        emailLayout = rootView.findViewById(R.id.email_layout);
        rollNumberLayout = rootView.findViewById(R.id.roll_number_layout);
        phoneLayout = rootView.findViewById(R.id.phone_layout);
        parentPhoneLayout = rootView.findViewById(R.id.parent_phone_layout);
        dobLayout = rootView.findViewById(R.id.dob_layout);
        schoolLayout = rootView.findViewById(R.id.school_layout);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        uid = user.getUid();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        String ID = sharedPreferences.getString("id", "A001");
        String std = ID.substring(0,1);
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

        mDbRef = FirebaseDatabase.getInstance().getReference("students").child(standard).child(ID);

        if (!uid.isEmpty()) {
            getStudentData();
        }

        rootView.findViewById(R.id.follow).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        rootView.findViewById(R.id.contact_teacher).setOnClickListener(view -> {
            Snackbar bar = Snackbar.make(profileLayout, "Opening WhatsApp...", Snackbar.LENGTH_LONG);
            bar.show();

            String phoneNumberWithCountryCode = "+918104810125";
            String message = "Hello Bhaiya, I got a question!";

            try {
                PackageManager packageManager = requireContext().getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                String url = "https://api.whatsapp.com/send?phone=" + phoneNumberWithCountryCode + "&text=" + URLEncoder.encode(message, "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));

                if (i.resolveActivity(packageManager) != null) {
                    requireContext().startActivity(i);
                } else {
                    Snackbar snackbar = Snackbar.make(profileLayout, "WhatsApp not Installed!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(profileLayout, "WhatsApp not Installed!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        return rootView;

    }

    private void getStudentData() {
        mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String DP = snapshot.child("profileImageUrl").getValue(String.class);
                String Name = snapshot.child("name").getValue(String.class);
                String Email = snapshot.child("email").getValue(String.class);
                String RollNumber = snapshot.child("id").getValue(String.class);
                String Phone = snapshot.child("phone").getValue(String.class);
                String ParentPhone = snapshot.child("parentPhone").getValue(String.class);
                String DOB = snapshot.child("DOB").getValue(String.class);
                String School = snapshot.child("school").getValue(String.class);

                if (DP == null || DP.equals("")) {
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