package com.dev.mentormenteesystem.mentor.adapter;

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
import com.dev.mentormenteesystem.mentor.model.MenteeRequestModel;
import com.dev.mentormenteesystem.mentor.model.SubjectsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenteeRequestToMentorAdapter extends RecyclerView.Adapter<MenteeRequestToMentorAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<MenteeRequestModel> list;
    FirebaseAuth auth;
    DatabaseReference dbReferenceRequests;
    DatabaseReference dbReferenceStatus;
    ProgressDialog progressDialog;

    public MenteeRequestToMentorAdapter(Context mContext, ArrayList<MenteeRequestModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_mentee_requests, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MenteeRequestModel data = list.get(position);
        holder.tvFullName.setText(data.getFullName());
        holder.tvSubject.setText(data.getSubject());
        holder.tvGroup.setText(data.getGroup());
        holder.tvSemester.setText(data.getSemester());

        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApproveMethod(data);
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRejectMethod(data);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullName, tvGroup, tvSemester, tvSubject;
        AppCompatButton btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvGroup = itemView.findViewById(R.id.tvGroup);
            tvSemester = itemView.findViewById(R.id.tvSemester);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    private void callApproveMethod(MenteeRequestModel data){
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Going Good...");
        progressDialog.setMessage("It takes Just a few Seconds... ");
        progressDialog.setIcon(R.drawable.happy);
        progressDialog.setCancelable(false);
        progressDialog.show();
        auth = FirebaseAuth.getInstance();
        dbReferenceStatus = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("MenteeRequestsStatus").child(data.getUserUID()).child(data.getUserPushID());
        Map<String, Object> update = new HashMap<String, Object>();
        update.put("status", "Approved");
        dbReferenceStatus.updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dbReferenceRequests = FirebaseDatabase.getInstance(
                        "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("MenteeRequestsToMentors").
                child(auth.getCurrentUser().getUid()).child(data.getMentorPushID());
                dbReferenceRequests.removeValue();
                notifyDataSetChanged();
                progressDialog.dismiss();
                Toast.makeText(mContext, "Approved", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void callRejectMethod(MenteeRequestModel data){
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Going Good...");
        progressDialog.setMessage("It takes Just a few Seconds... ");
        progressDialog.setIcon(R.drawable.happy);
        progressDialog.setCancelable(false);
        progressDialog.show();
        auth = FirebaseAuth.getInstance();

        dbReferenceStatus = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("MenteeRequestsStatus").child(data.getUserUID()).child(data.getUserPushID());
        Map<String, Object> update = new HashMap<String, Object>();
        update.put("status", "Rejected");
        dbReferenceStatus.updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dbReferenceRequests = FirebaseDatabase.getInstance(
                                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("MenteeRequestsToMentors").
                        child(auth.getCurrentUser().getUid()).child(data.getMentorPushID());
                dbReferenceRequests.removeValue();
                notifyDataSetChanged();
                progressDialog.dismiss();
                Toast.makeText(mContext, "Rejected", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
