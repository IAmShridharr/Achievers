package com.infiniterealm.achievers.students.mainFragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.students.activities.EditProfileActivity;
import com.infiniterealm.achievers.students.helper.StudentRepository;
import com.infiniterealm.achievers.students.models.StudentProfileModel;
import com.infiniterealm.achievers.utilities.Essentials;
import com.infiniterealm.achievers.utilities.SnackBarHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    View rootView;
    TextView name, rollNumber, dob, school, followers, tests, followings;
    ProgressBar progressBar;
    ConstraintLayout studentProfileLayout;
    LinearLayout dobLayout, schoolLayout;
    MaterialButton edit, contactTeacher;
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

        defineLayout();

        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String ID = sharedPreferences.getString("id", null);

        getAndDisplayStudentData(ID);

        edit.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        contactTeacher.setOnClickListener(view -> {
            SnackBarHelper.showShortSnackBar(studentProfileLayout, "Opening WhatsApp...");

            String phoneNumberWithCountryCode = "+918432312431";
            String message = "Hello Bhaiya, I got a question!";

            Essentials.openWhatsApp(phoneNumberWithCountryCode, message, requireContext());

        });

        return rootView;

    }

    private void getAndDisplayStudentData(String studentID) {
        StudentRepository studentRepository = new StudentRepository();
        studentRepository.getStudentData(studentID, new StudentRepository.OnStudentDataListener() {
            @Override
            public void onStudentDataLoaded(StudentProfileModel student) {
                // Update the UI with the fetched data
                followers.setText(String.valueOf(student.getFollowers()));
                tests.setText(String.valueOf(student.getTests()));
                followings.setText(String.valueOf(student.getFollowings()));

                name.setText(student.getName());
                rollNumber.setText(student.getId());

                // ... and so on
                if (student.getProfileImageURL() == null || student.getProfileImageURL().equals("")) {
                    profileImage.setImageResource(R.drawable.profile_picture_placeholder);
                } else {
                    if (isAdded()) {
                        Glide.with(requireContext())
                                .load(student.getProfileImageURL())
                                .into(profileImage);
                    }
                }
                assert student.getDOB() != null;
                if (student.getDOB().isEmpty()) {
                    dobLayout.setVisibility(View.GONE);
                } else {
                    dobLayout.setVisibility(View.VISIBLE);
                    dob.setText(student.getDOB());
                }
                assert student.getSchool() != null;
                if (student.getSchool().isEmpty()) {
                    schoolLayout.setVisibility(View.GONE);
                } else {
                    schoolLayout.setVisibility(View.VISIBLE);
                    school.setText(student.getSchool());
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onStudentDataError(String errorMessage) {
                // Handle error
                SnackBarHelper.showShortSnackBar(studentProfileLayout, errorMessage);
            }
        });
    }

    private void defineLayout() {
        studentProfileLayout = rootView.findViewById(R.id.studentProfileLayout);
        dobLayout = rootView.findViewById(R.id.dob_layout);
        schoolLayout = rootView.findViewById(R.id.school_layout);

        profileImage = rootView.findViewById(R.id.profile_image);

        name = rootView.findViewById(R.id.name);
        rollNumber = rootView.findViewById(R.id.roll_number);
        dob = rootView.findViewById(R.id.dob);
        school = rootView.findViewById(R.id.school);

        followers = rootView.findViewById(R.id.followers);
        tests = rootView.findViewById(R.id.tests);
        followings = rootView.findViewById(R.id.followings);

        edit = rootView.findViewById(R.id.edit);
        contactTeacher = rootView.findViewById(R.id.contact_teacher);

        progressBar = rootView.findViewById(R.id.progressBar);
    }
}