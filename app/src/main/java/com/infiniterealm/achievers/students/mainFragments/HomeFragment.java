package com.infiniterealm.achievers.students.mainFragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.infiniterealm.achievers.LoginActivity;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.students.activities.StudentChatsActivity;
import com.infiniterealm.achievers.students.activities.StudentNotificationActivity;
import com.infiniterealm.achievers.students.activities.StudentPostActivity;
import com.infiniterealm.achievers.students.activities.StudentSearchActivity;
import com.infiniterealm.achievers.utilities.Essentials;

import java.util.ArrayList;
import java.util.List;

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
    ImageView search, notifications, addPost, chats;
    DatabaseReference mDbRef, authRef;
    ConstraintLayout studentHomeLayout;
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

        studentHomeLayout = rootView.findViewById(R.id.studentHomeLayout);

        search = rootView.findViewById(R.id.search);
        notifications = rootView.findViewById(R.id.notifications);
        addPost = rootView.findViewById(R.id.addPost);
        chats = rootView.findViewById(R.id.chats);

        search.setColorFilter(Essentials.getColor(requireContext(), R.color.pink_900, R.color.pink_50));
        notifications.setColorFilter(Essentials.getColor(requireContext(), R.color.pink_900, R.color.pink_50));
        addPost.setColorFilter(Essentials.getColor(requireContext(), R.color.pink_900, R.color.pink_50));
        chats.setColorFilter(Essentials.getColor(requireContext(), R.color.pink_900, R.color.pink_50));


        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        String ID = sharedPreferences.getString("id", null);
        assert ID != null;
        String standard = Essentials.getStandard(ID);

        mDbRef = FirebaseDatabase.getInstance().getReference("students").child(standard).child(ID);

        String device = Build.MANUFACTURER + " - " + Build.MODEL;
        authRef = FirebaseDatabase.getInstance().getReference("Auth").child(ID).child("Sessions").child(device);

        notifications.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), StudentNotificationActivity.class);
            startActivity(intent);
        });

        addPost.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), StudentPostActivity.class);
            startActivity(intent);
        });

        chats.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), StudentChatsActivity.class);
            startActivity(intent);
        });

        search.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), StudentSearchActivity.class);
            startActivity(intent);
        });

        rootView.findViewById(R.id.studentLogout).setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            authRef.child("lastLoggedOut").setValue(System.currentTimeMillis());
            authRef.child("isLoggedIn").setValue(false);
            //Your code
            mAuth.signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        return rootView;
    }
}