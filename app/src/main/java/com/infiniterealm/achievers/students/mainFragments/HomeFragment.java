package com.infiniterealm.achievers.students.mainFragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infiniterealm.achievers.LoginActivity;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.students.activities.StudentProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    SharedPreferences sharedPreferences;
    View rootView;
    FirebaseAuth mAuth;
    EditText search;
    FirebaseUser user;
    DatabaseReference mDbRef;
    private List<String> itemList = new ArrayList<>();
    private List<String> originalItemList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

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
        search = rootView.findViewById(R.id.input_search);

        // Initialize the ListView and Adapter
        ListView listView = rootView.findViewById(R.id.searchItemList);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference("students");
        studentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the itemList to avoid duplicate entries
                itemList.clear();

                // Loop through the children nodes (i.e., "5th Standard", "7th Standard", etc.)
                for (DataSnapshot standardSnapshot : dataSnapshot.getChildren()) {
//                    Log.d("test", standardSnapshot.getRef().toString());
                    // Loop through the student ids within each standard
                    for (DataSnapshot studentSnapshot : standardSnapshot.getChildren()) {
//                        Log.d("test", studentSnapshot.getRef().toString());
                        // Loop through the children within each student id node
                        for (DataSnapshot childrenSnapshot : studentSnapshot.getChildren()) {
//                            Log.d("test", childrenSnapshot.getRef().toString());
                            if (Objects.equals(childrenSnapshot.getKey(), "Profile Information")) {
//                               Log.d("test", childrenSnapshot.getRef().toString());
                                // Assuming each Personal Information node has a "name" and "rollNumber" fields
                                String name = childrenSnapshot.child("name").getValue(String.class);
                                String rollNumber = childrenSnapshot.child("id").getValue(String.class);

                                if (name != null) {
                                    itemList.add(name + ": " + rollNumber);

                                    // Copy the contents of itemList to originalItemList
                                    originalItemList.add(name + ": " + rollNumber);
                                }
                            }
                        }
                    }
                }

                // Update the adapter to reflect the new data
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur while fetching data

            }
        });

        mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String roll = dataSnapshot.child("id").getValue(String.class);

                // Use the retrieved name value as needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error case
            }
        });

        // Set OnItemClickListener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the clicked item from the adapter
                String clickedItem = adapter.getItem(position);
                assert clickedItem != null;
                String Name = clickedItem.substring(0, clickedItem.length()-6);
                String ID = clickedItem.substring(clickedItem.length()-4);
                itemList.clear();
                adapter.notifyDataSetChanged();

                Intent intent = new Intent(requireContext(), StudentProfileActivity.class);
                intent.putExtra("name", Name);
                intent.putExtra("ID", ID);
                startActivity(intent);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Get the entered text from the search EditText
                String searchText = charSequence.toString().trim();

                // Clear the itemList to avoid duplicate entries
                itemList.clear();

                // If the search text is empty, show the hint
                if (searchText.isEmpty()) {
                    search.setHint("for your Classmates, Friends etc.");
                    // Add all original data to the itemList
                    itemList.addAll(originalItemList);

                    listView.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    // If search text is not empty, filter data based on the search text
                    for (String name : originalItemList) {
                        if (name.toLowerCase().contains(searchText.toLowerCase())) {
                            itemList.add(name);
                        }
                    }
                }

                // Update the adapter to reflect the new data
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rootView.findViewById(R.id.input_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setHint("for your Classmates, Friends etc.");
            }
        });

        rootView.findViewById(R.id.studentLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                //Your code
                mAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        return rootView;
    }

    private void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}