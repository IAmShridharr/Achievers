package com.infiniterealm.achievers.utilities;

import static com.infiniterealm.achievers.admins.adapters.StudentsAdapter.MY_PERMISSIONS_REQUEST_CALL_PHONE;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infiniterealm.achievers.network.NetworkUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Essentials {

    public static String getStandard(String ID) {
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
        return standard;
    }

    public static int getColor(Context context, int lightModeColorRes, int darkModeColorRes) {
        Configuration configuration = context.getResources().getConfiguration();
        int currentMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (currentMode == Configuration.UI_MODE_NIGHT_YES) {
            return ContextCompat.getColor(context, darkModeColorRes);
        } else {
            return ContextCompat.getColor(context, lightModeColorRes);
        }
    }

    public static boolean isOtherActivityRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            // Get a list of running tasks (activities) with the most recent task at index 0
            // The list may contain activities from different apps, not just your app
            // So, make sure to filter the tasks based on your app's package name
            String packageName = context.getPackageName();
            for (ActivityManager.RunningTaskInfo taskInfo : activityManager.getRunningTasks(1)) {
                assert taskInfo.baseActivity != null;
                if (!packageName.equals(taskInfo.baseActivity.getPackageName())) {
                    // At least one other activity is running
                    return true;
                }
            }
        }
        // No other activity is running
        return false;
    }

    public static boolean checkNetwork(NetworkUtils networkUtils) {
        final boolean[] isConnected = {false};
        networkUtils.checkNetworkStatus(new NetworkUtils.OnNetworkChangeListener() {
            @Override
            public void onNetworkAvailable() {
                isConnected[0] = true;
            }

            @Override
            public void onNetworkUnavailable() {
                isConnected[0] = false;
            }
        });
        return isConnected[0];
    }

    public static void isOnline(String ID) {
        DatabaseReference studentOnlineStatusRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Students")
                .child(Essentials.getStandard(ID))
                .child(ID)
                .child("Chats")
                .child("isOnline");

        // Set the initial online status to true (user is online)
        studentOnlineStatusRef.setValue(true);

        // When the app disconnects (loses connection), set the online status to false
        studentOnlineStatusRef.onDisconnect().setValue(false);

        ValueEventListener onlineStatusListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isOnline = Boolean.TRUE.equals(dataSnapshot.getValue(Boolean.class));
                if (isOnline) {
                    // Student is online
                } else {
                    // Student is offline
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        };

        studentOnlineStatusRef.addValueEventListener(onlineStatusListener);
    }

    public static void setOnlineStatus(boolean isOnline, String ID) {
        // Set the online status in Firebase
        String Standard = Essentials.getStandard(ID);
        DatabaseReference studentOnlineStatusRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("students")
                .child(Standard)
                .child(ID)
                .child("Chats")
                .child("isOnline");
        studentOnlineStatusRef.setValue(isOnline);
    }

    public static void updateLastSeen(String ID) {
        String Standard = Essentials.getStandard(ID);
        FirebaseDatabase.getInstance()
                .getReference().child("students")
                .child(Standard)
                .child(ID)
                .child("Chats")
                .child("lastSeen").setValue(System.currentTimeMillis());
    }

    public static void openPhoneApp(String phoneNumber, Context context) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            context.startActivity(callIntent);
        }
    }

    public static void openWhatsApp(String phoneNumberWithCountryCode, String message, Context context) {
        try {
            String url = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                url = "https://api.whatsapp.com/send?phone=" + phoneNumberWithCountryCode + "&text=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
            } else {
                url = "https://api.whatsapp.com/send?phone=" + phoneNumberWithCountryCode + "&text=" + message;
            }
            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

            // Check if WhatsApp and WhatsApp Business are installed
            if (isAppInstalled("com.whatsapp", context) && isAppInstalled("com.whatsapp.w4b", context)) {
                Intent chooserIntent = Intent.createChooser(whatsappIntent, "Choose WhatsApp or WhatsApp Business");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { whatsappIntent });
                context.startActivity(chooserIntent);
            } else {
                // If only one of WhatsApp or WhatsApp Business is installed or neither is installed, open directly
                String packageName = isAppInstalled("com.whatsapp", context) ? "com.whatsapp" : "com.whatsapp.w4b";
                whatsappIntent.setPackage(packageName);
                context.startActivity(whatsappIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isAppInstalled(String packageName, Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
