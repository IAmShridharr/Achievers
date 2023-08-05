package com.infiniterealm.achievers.students.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.utilities.Essentials;
import com.infiniterealm.achievers.utilities.SnackBarHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentSearchActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText search;
    ConstraintLayout studentSearchLayout;
    DatabaseReference mDbRef;
    private List<String> itemList = new ArrayList<>();
    private List<String> originalItemList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_search);

        studentSearchLayout = findViewById(R.id.studentSearchLayout);

        sharedPreferences = getApplicationContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        String ID = sharedPreferences.getString("id", null);
        assert ID != null;
        String standard = Essentials.getStandard(ID);

        mDbRef = FirebaseDatabase.getInstance().getReference("students").child(standard).child(ID);
        search = findViewById(R.id.input_search);

        // Initialize the ListView and Adapter
        ListView listView = findViewById(R.id.searchItemList);
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, itemList);
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
//                String name = dataSnapshot.child("name").getValue(String.class);
//                String roll = dataSnapshot.child("id").getValue(String.class);

                // Use the retrieved name value as needed

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error case
            }
        });

        // Set OnItemClickListener for the ListView
        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            // Get the clicked item from the adapter
            String clickedItem = adapter.getItem(position);
            assert clickedItem != null;
            String Name = clickedItem.substring(0, clickedItem.length()-6);
            String iD = clickedItem.substring(clickedItem.length()-4);
            itemList.clear();
            adapter.notifyDataSetChanged();

            if (iD.equals(ID)) {
                SnackBarHelper.showShortSnackBar(studentSearchLayout, "Its you!");
            } else {
                Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                intent.putExtra("name", Name);
                intent.putExtra("ID", iD);
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
                    search.setHint("Search by roll number or name");
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

        search.setOnFocusChangeListener((view, b) -> {
            search.setHint("Search by roll number or name");
            InputMethodManager imm = ContextCompat.getSystemService(getApplicationContext(), InputMethodManager.class);
            if (imm != null) {
                imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }
}