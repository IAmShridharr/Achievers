package com.infiniterealm.achievers.admins.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.admins.activities.EditStudentProfilesActivity;
import com.infiniterealm.achievers.admins.models.StudentListItemModel;
import com.infiniterealm.achievers.utilities.Essentials;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentViewHolder> {

    private final Context context;
    private List<StudentListItemModel> studentsList;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

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

        Glide.with(holder.studentProfileImage.getContext()).load(model.getProfileImageURL()).placeholder(R.drawable.profile_picture_placeholder).into(holder.studentProfileImage);

        holder.studentProfileImage.setOnClickListener(view -> {
            Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            ImageView imageView = new ImageView(context);
            imageView.setImageDrawable(holder.studentProfileImage.getDrawable());
            dialog.setContentView(imageView);
            dialog.show();
        });

        holder.call.setOnClickListener(view -> {
            String phone = model.getPhone();
            Essentials.openPhoneApp(phone, context);
        });

        holder.details.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditStudentProfilesActivity.class);
            intent.putExtra("id", model.getId());
            context.startActivity(intent);
        });

        holder.whatsapp.setOnClickListener(view -> {
            String phoneNumberWithCountryCode = model.getPhone();
            String message = "Hello, " + model.getName() + "!";

            Essentials.openWhatsApp(phoneNumberWithCountryCode, message, context);
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
}
