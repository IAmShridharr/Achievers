package com.infiniterealm.achievers.admins.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.admins.activities.EditStudentProfilesActivity;
import com.infiniterealm.achievers.admins.mainFragments.AdminProfileFragment;
import com.infiniterealm.achievers.admins.models.StudentListItemModel;
import com.infiniterealm.achievers.students.activities.EditProfileActivity;

import java.net.URLEncoder;

public class StudentAdapter extends FirebaseRecyclerAdapter<StudentListItemModel, StudentAdapter.StudentViewHolder> {
    Context context;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public StudentAdapter(@NonNull FirebaseRecyclerOptions<StudentListItemModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull StudentViewHolder holder, int position, @NonNull StudentListItemModel model) {

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
            context.startActivity(intent);
        });

        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumberWithCountryCode = model.getPhone();
                String message = "Hello, " + model.getName() + "!";

                try {
                    PackageManager packageManager = context.getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);

                    String url = "https://api.whatsapp.com/send?phone=" + phoneNumberWithCountryCode + "&text=" + URLEncoder.encode(message, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));

                    if (i.resolveActivity(packageManager) != null) {
                        context.startActivity(i);
                    } else {
                        Snackbar snackbar = Snackbar.make(holder.studentProfileImage, "WhatsApp not Installed!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(holder.studentProfileImage, "WhatsApp not Installed!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
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

}
