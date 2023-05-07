package com.infiniterealm.achievers.admins.mainFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.admins.activities.AddStudentActivity;
import com.infiniterealm.achievers.admins.adapters.StudentAdapter;
import com.infiniterealm.achievers.admins.models.StudentListItemModel;
import com.infiniterealm.achievers.students.mainFragments.HomeFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminProfileFragment extends Fragment {

    View rootView;
    RecyclerView studentList;
    StudentAdapter studentAdapter;

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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();

        studentList = rootView.findViewById(R.id.studentsList);
        studentList.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users");

        FirebaseRecyclerOptions<StudentListItemModel> options =
                new FirebaseRecyclerOptions.Builder<StudentListItemModel>()
                        .setQuery(query, StudentListItemModel.class)
                        .build();

        studentAdapter = new StudentAdapter(options, requireContext());
        studentList.setAdapter(studentAdapter);

        addStudent.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddStudentActivity.class);
            startActivity(intent);
        });


        return rootView;
    }

    private void setUpRecyclerView() {
        studentList = rootView.findViewById(R.id.studentsList);
        studentList.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users");

        FirebaseRecyclerOptions<StudentListItemModel> options =
                new FirebaseRecyclerOptions.Builder<StudentListItemModel>()
                        .setQuery(query, StudentListItemModel.class)
                        .build();

        studentAdapter = new StudentAdapter(options, requireContext());
        studentList.setAdapter(studentAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        studentAdapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        studentAdapter.stopListening();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        studentAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpRecyclerView();
        studentAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        studentAdapter.stopListening();
    }
}