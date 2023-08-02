package com.infiniterealm.achievers.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.infiniterealm.achievers.R;

public class SnackBarHelper {

    public static void showShortSnackBar(View showOnLayout, String message) {
        Snackbar snackbar = Snackbar.make(showOnLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void showLongSnackBar(View showOnLayout, String message) {
        Snackbar snackbar = Snackbar.make(showOnLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void showIndefiniteSnackBar(View showOnLayout, String message) {
        Snackbar snackbar = Snackbar.make(showOnLayout, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    public static void showShortSnackBarWithAnchorView(View showOnLayout, String message, View anchorView) {
        Snackbar snackbar = Snackbar.make(showOnLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(anchorView);
        snackbar.show();
    }

    public static void showLongSnackBarWithAnchorView(View showOnLayout, String message, View anchorView) {
        Snackbar snackbar = Snackbar.make(showOnLayout, message, Snackbar.LENGTH_LONG);
        snackbar.setAnchorView(anchorView);
        snackbar.show();
    }

    public static void showIndefiniteSnackBarWithAnchorView(View showOnLayout, String message, View anchorView) {
        Snackbar snackbar = Snackbar.make(showOnLayout, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAnchorView(anchorView);
        snackbar.show();
    }

    public static void showShortCustomSnackBar(Context context, View showOnLayout, String snackBarTitleText, String actionText1, String actionText2) {
        View customSnackBarView = LayoutInflater.from(context).inflate(R.layout.custom_snackbar, (ViewGroup) showOnLayout, false);

        MaterialTextView snackBarTitle = customSnackBarView.findViewById(R.id.snackBarTitle);

        TextView actionButton1 = customSnackBarView.findViewById(R.id.action1);
        TextView actionButton2 = customSnackBarView.findViewById(R.id.action2);

        snackBarTitle.setText(snackBarTitleText);

        actionButton1.setText(actionText1);
        actionButton2.setText(actionText2);

        Snackbar snackbar = Snackbar.make(showOnLayout,"", Snackbar.LENGTH_SHORT);
        View customView = snackbar.getView();
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) customView;

        // Inflate the custom layout and add it to the SnackBar layout
        snackbarLayout.addView(customSnackBarView, 0);

        snackbar.show();
    }

    public static void showLongCustomSnackBar(Context context, View showOnLayout, String snackBarTitleText, String actionText1, String actionText2) {
        View customSnackBarView = LayoutInflater.from(context).inflate(R.layout.custom_snackbar, (ViewGroup) showOnLayout, false);

        TextView snackBarTitle = customSnackBarView.findViewById(R.id.snackBarTitle);

        TextView actionButton1 = customSnackBarView.findViewById(R.id.action1);
        TextView actionButton2 = customSnackBarView.findViewById(R.id.action2);

        snackBarTitle.setText(snackBarTitleText);

        actionButton1.setText(actionText1);
        actionButton2.setText(actionText2);

        Snackbar snackbar = Snackbar.make(showOnLayout,"", Snackbar.LENGTH_LONG);
        View customView = snackbar.getView();
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) customView;

        // Inflate the custom layout and add it to the SnackBar layout
        snackbarLayout.addView(customSnackBarView, 0);

        snackbar.show();
    }

    public static void showIndefiniteCustomSnackBar(Context context, View showOnLayout, String snackBarTitleText, String actionText1, String actionText2) {
        View customSnackBarView = LayoutInflater.from(context).inflate(R.layout.custom_snackbar, (ViewGroup) showOnLayout, false);

        TextView snackBarTitle = customSnackBarView.findViewById(R.id.snackBarTitle);

        TextView actionButton1 = customSnackBarView.findViewById(R.id.action1);
        TextView actionButton2 = customSnackBarView.findViewById(R.id.action2);

        snackBarTitle.setText(snackBarTitleText);

        actionButton1.setText(actionText1);
        actionButton2.setText(actionText2);

        Snackbar snackbar = Snackbar.make(showOnLayout,"", Snackbar.LENGTH_INDEFINITE);
        View customView = snackbar.getView();
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) customView;

        // Inflate the custom layout and add it to the SnackBar layout
        snackbarLayout.addView(customSnackBarView, 0);

        snackbar.show();
    }

    public static void showShortCustomSnackBarWithAnchorView(Context context, View showOnLayout, View anchorView, String snackBarTitleText, String actionText1, String actionText2) {
        View customSnackBarView = LayoutInflater.from(context).inflate(R.layout.custom_snackbar, (ViewGroup) showOnLayout, false);

        TextView snackBarTitle = customSnackBarView.findViewById(R.id.snackBarTitle);

        TextView actionButton1 = customSnackBarView.findViewById(R.id.action1);
        TextView actionButton2 = customSnackBarView.findViewById(R.id.action2);

        snackBarTitle.setText(snackBarTitleText);

        actionButton1.setText(actionText1);
        actionButton2.setText(actionText2);

        Snackbar snackbar = Snackbar.make(showOnLayout,"", Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(anchorView);
        View customView = snackbar.getView();
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) customView;

        // Inflate the custom layout and add it to the SnackBar layout
        snackbarLayout.addView(customSnackBarView, 0);

        snackbar.show();
    }

    public static void showLongCustomSnackBarWithAnchorView(Context context, View showOnLayout, View anchorView, String snackBarTitleText, String actionText1, String actionText2) {
        View customSnackBarView = LayoutInflater.from(context).inflate(R.layout.custom_snackbar, (ViewGroup) showOnLayout, false);

        TextView snackBarTitle = customSnackBarView.findViewById(R.id.snackBarTitle);

        TextView actionButton1 = customSnackBarView.findViewById(R.id.action1);
        TextView actionButton2 = customSnackBarView.findViewById(R.id.action2);

        snackBarTitle.setText(snackBarTitleText);

        actionButton1.setText(actionText1);
        actionButton2.setText(actionText2);

        Snackbar snackbar = Snackbar.make(showOnLayout,"", Snackbar.LENGTH_LONG);
        snackbar.setAnchorView(anchorView);
        View customView = snackbar.getView();
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) customView;

        // Inflate the custom layout and add it to the SnackBar layout
        snackbarLayout.addView(customSnackBarView, 0);

        snackbar.show();
    }

    public static void showIndefiniteCustomSnackBarWithAnchorView(Context context, View showOnLayout, View anchorView, String snackBarTitleText, String actionText1, String actionText2) {
        View customSnackBarView = LayoutInflater.from(context).inflate(R.layout.custom_snackbar, (ViewGroup) showOnLayout, false);

        TextView snackBarTitle = customSnackBarView.findViewById(R.id.snackBarTitle);

        TextView actionButton1 = customSnackBarView.findViewById(R.id.action1);
        TextView actionButton2 = customSnackBarView.findViewById(R.id.action2);

        snackBarTitle.setText(snackBarTitleText);

        actionButton1.setText(actionText1);
        actionButton2.setText(actionText2);

        Snackbar snackbar = Snackbar.make(showOnLayout,"", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAnchorView(anchorView);
        View customView = snackbar.getView();
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) customView;

        // Inflate the custom layout and add it to the SnackBar layout
        snackbarLayout.addView(customSnackBarView, 0);

        snackbar.show();
    }
}
