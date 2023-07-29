package com.infiniterealm.achievers.admins.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.admins.activities.EditStudentProfilesActivity;
import com.infiniterealm.achievers.admins.models.StudentListItemModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentViewHolder> {

    private Context context;
    private List<StudentListItemModel> studentsList;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    public StudentsAdapter(Context context, List<StudentListItemModel> studentsList) {
        this.context = context;
        this.studentsList = studentsList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentListItemModel model = studentsList.get(position);

        // Bind the student data to the ViewHolder views
        if (!model.getName().isEmpty() || !model.getName().equals("")) {
            holder.studentName.setVisibility(View.VISIBLE);
            holder.studentName.setText(model.getName());
        } else {
            holder.studentName.setVisibility(View.GONE);
        }

        if (!model.getId().isEmpty() || !model.getId().equals("")) {
            holder.studentRollNumber.setVisibility(View.VISIBLE);
            holder.studentRollNumber.setText(model.getId());
        } else {
            holder.studentRollNumber.setVisibility(View.GONE);
        }

        Glide.with(holder.studentProfileImage.getContext()).load(model.getProfileImageUrl()).placeholder(R.drawable.profile_picture_placeholder).into(holder.studentProfileImage);

        holder.studentProfileImage.setOnClickListener(view -> {
            Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            ImageView imageView = new ImageView(context);
            imageView.setImageDrawable(holder.studentProfileImage.getDrawable());
            dialog.setContentView(imageView);
            dialog.show();
        });

        holder.call.setOnClickListener(view -> {
            String phone = model.getPhone();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {
                context.startActivity(callIntent);
            }
        });

        holder.details.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditStudentProfilesActivity.class);
            intent.putExtra("id", model.getId());
            context.startActivity(intent);
        });

        holder.whatsapp.setOnClickListener(view -> {
            String phoneNumberWithCountryCode = model.getPhone();
            String message = "Hello, " + model.getName() + "!";

            try {
                PackageManager packageManager = context.getPackageManager();

                String url = "https://api.whatsapp.com/send?phone=" + phoneNumberWithCountryCode + "&text=" + URLEncoder.encode(message, "UTF-8");

                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                whatsappIntent.setData(Uri.parse(url));
                whatsappIntent.setPackage("com.whatsapp");

                Intent whatsappBusinessIntent = new Intent(Intent.ACTION_VIEW);
                whatsappBusinessIntent.setData(Uri.parse(url));
                whatsappBusinessIntent.setPackage("com.whatsapp.w4b");

                if (whatsappBusinessIntent.resolveActivity(packageManager) != null) {
                    context.startActivity(whatsappBusinessIntent);
                } else {
                    showSnackBar(view, "App not Installed!");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                showSnackBar(view, "Failed to send message!");
            } catch (Exception e) {
                e.printStackTrace();
                showSnackBar(view, "Something went wrong...!");
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        ImageView studentProfileImage;
        TextView studentName, studentRollNumber;
        ImageButton call, whatsapp;
        ConstraintLayout layout;
        LinearLayout details;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentProfileImage = itemView.findViewById(R.id.studentProfileImage);

            studentName = itemView.findViewById(R.id.studentName);
            studentRollNumber = itemView.findViewById(R.id.studentRollNumber);

            call = itemView.findViewById(R.id.call);
            whatsapp = itemView.findViewById(R.id.whatsapp);

            layout = itemView.findViewById(R.id.adminProfileLayout);
            details = itemView.findViewById(R.id.details);
        }
    }

    private void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
