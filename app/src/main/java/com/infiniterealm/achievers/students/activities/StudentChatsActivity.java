package com.infiniterealm.achievers.students.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.network.NetworkUtils;
import com.infiniterealm.achievers.utilities.Essentials;
import com.infiniterealm.achievers.utilities.SnackBarHelper;

public class StudentChatsActivity extends AppCompatActivity {

    ConstraintLayout studentChatLayout;
    private NetworkUtils networkUtils;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_chats);

        studentChatLayout = findViewById(R.id.studentChatLayout);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        ID = sharedPreferences.getString("id", null);

        networkUtils = new NetworkUtils(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Essentials.checkNetwork(new NetworkUtils(this))) {
            SnackBarHelper.showShortSnackBar(studentChatLayout, "No Internet!");
        }
        Essentials.updateLastSeen(ID);
        Essentials.setOnlineStatus(true, ID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Essentials.updateLastSeen(ID);
        Essentials.setOnlineStatus(false, ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the network callback to avoid leaks
        networkUtils.unregisterNetworkCallback();
    }
}