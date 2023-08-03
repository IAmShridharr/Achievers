package com.infiniterealm.achievers.admins.mainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.admins.activities.AddStudentActivity;
import com.infiniterealm.achievers.admins.adapters.StudentsAdapter;
import com.infiniterealm.achievers.admins.models.StudentListItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminProfileFragment extends Fragment {

    View rootView;
    RecyclerView studentList;
    StudentsAdapter studentsAdapter;

    Button addStudent;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminProfileFragment newInstance(String param1, String param2) {
        AdminProfileFragment fragment = new AdminProfileFragment();
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
        rootView = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        addStudent = rootView.findViewById(R.id.addStudent);

        studentList = rootView.findViewById(R.id.studentsList);
        studentList.setLayoutManager(new LinearLayoutManager(getContext()));

        setUpRecyclerView();

        addStudent.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddStudentActivity.class);
            startActivity(intent);
        });

        return rootView;
    }

    private void setUpRecyclerView() {
        studentList = rootView.findViewById(R.id.studentsList);
        studentList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get a reference to the "students" node in the database
        DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference().child("students");

        // Fetch all the students
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                // Create a list to hold the students
                List<StudentListItemModel> studentsList = new ArrayList<>();

                // Loop through the children nodes (i.e., "5th Standard", "7th Standard", etc.)
                for (DataSnapshot standardSnapshot : dataSnapshot.getChildren()) {
                    // Loop through the students within each standard
                    for (DataSnapshot studentSnapshot : standardSnapshot.getChildren()) {
                        // Loop through the profile information within each student
                        for (DataSnapshot profileSnapshot : studentSnapshot.getChildren()) {
                            if (studentSnapshot.hasChild("Profile Information")) {
                                // Get the student object using the Student class
                                StudentListItemModel student = profileSnapshot.getValue(StudentListItemModel.class);
                                // Add the student to the list if it's not null
                                if (student != null) {
                                    studentsList.add(student);
                                }
                            }
                        }
                    }
                }

                // Initialize the RecyclerView and the Adapter
                studentList.setLayoutManager(new LinearLayoutManager(getContext()));
                studentsAdapter = new StudentsAdapter(getContext(), studentsList);
                studentList.setAdapter(studentsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors that occur while retrieving data
                Log.e("Student Data", "Error fetching students: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpRecyclerView();
    }
}