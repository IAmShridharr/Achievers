package com.infiniterealm.achievers.admins.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.infiniterealm.achievers.R;
import com.infiniterealm.achievers.admins.models.StudentListItemModel;

public class StudentAdapter extends FirebaseRecyclerAdapter<StudentListItemModel, StudentAdapter.StudentViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public StudentAdapter(@NonNull FirebaseRecyclerOptions<StudentListItemModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull StudentViewHolder holder, int position, @NonNull StudentListItemModel model) {
        holder.studentName.setText(model.getName());
//        holder.studentPhone.setText(model.getPhone());
        holder.studentRollNumber.setText(model.getId());
//        holder.studentSchool.setText(model.getSchool());
        Glide.with(holder.studentProfileImage.getContext()).load(model.getProfileImageUrl()).placeholder(R.drawable.profile_picture_placeholder).into(holder.studentProfileImage);
        holder.studentProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        TextView studentName, studentPhone, studentRollNumber, studentSchool;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            studentProfileImage = itemView.findViewById(R.id.studentProfileImage);
            studentName = itemView.findViewById(R.id.studentName);
//            studentPhone = itemView.findViewById(R.id.studentPhone);
            studentRollNumber = itemView.findViewById(R.id.studentRollNumber);
//            studentSchool = itemView.findViewById(R.id.studentSchool);
        }
    }

}
