package com.dev.mentormenteesystem.mentee.adaptor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dev.mentormenteesystem.R;
import com.dev.mentormenteesystem.mentor.model.ApprovedMentorModel;
import com.dev.mentormenteesystem.mentor.model.MentorPendingModel;
import com.dev.mentormenteesystem.mentor.model.SubjectsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvailableMentorAdapter extends RecyclerView.Adapter<AvailableMentorAdapter.ViewHolder> {
    private OnShareClickedListener mCallback;
    private Context mContext;
    ArrayList<SubjectsModel> list;

    public AvailableMentorAdapter(Context mContext, ArrayList<SubjectsModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<SubjectsModel> filterlist) {
        list = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_available_mentors, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SubjectsModel data = list.get(position);
        holder.tvFullName.setText(data.getFullName());
        holder.tvSubject.setText(data.getSubjectName());
        holder.tvStudents.setText(data.getLimitStudent()+" STUDENT ONLY");
        Glide.with(holder.ivProfile).load(data.getImage()).into(holder.ivProfile);

        holder.btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClicked(data, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivProfile;
        TextView tvFullName, tvSubject, tvStudents;
        AppCompatButton btnRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvStudents = itemView.findViewById(R.id.tvStudents);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            btnRequest = itemView.findViewById(R.id.btnRequest);
        }
    }

    public void setOnShareClickedListener(OnShareClickedListener mCallback) {
        this.mCallback = mCallback;
    }

    public interface OnShareClickedListener {
        void onClicked(SubjectsModel data, int position);
    }

}
