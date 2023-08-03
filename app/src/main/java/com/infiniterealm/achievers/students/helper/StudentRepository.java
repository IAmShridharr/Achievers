package com.infiniterealm.achievers.students.helper;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infiniterealm.achievers.students.models.StudentProfileModel;
import com.infiniterealm.achievers.utilities.Essentials;

public class StudentRepository {
    private DatabaseReference databaseReference;

    public StudentRepository() {

    }

    public void getStudentData(String studentId, final OnStudentDataListener listener) {
        String standard = Essentials.getStandard(studentId);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("students").child(standard).child(studentId);
        databaseReference.child("Profile Information").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Map the snapshot to your UserModel class (if necessary)
                    StudentProfileModel student = snapshot.getValue(StudentProfileModel.class);
                    if (student != null) {
                        listener.onStudentDataLoaded(student);
                    } else {
                        listener.onStudentDataError("User data is null.");
                    }
                } else {
                    listener.onStudentDataError("User data not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onStudentDataError("Something went wrong!");
            }
        });
    }

    // Define a listener interface to handle data loading and errors
    public interface OnStudentDataListener {
        void onStudentDataLoaded(StudentProfileModel student);
        void onStudentDataError(String errorMessage);
    }
}
