package com.infiniterealm.achievers.utilities;

import static com.infiniterealm.achievers.admins.adapters.StudentsAdapter.MY_PERMISSIONS_REQUEST_CALL_PHONE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public static void updateLastSeen(String ID, String Device) {
        FirebaseDatabase.getInstance().getReference().child("Auth").child(ID).child("Sessions").child(Device).child("lastSeen").setValue(System.currentTimeMillis());
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
