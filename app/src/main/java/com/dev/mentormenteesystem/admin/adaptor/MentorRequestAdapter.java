package com.dev.mentormenteesystem.admin.adaptor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import com.dev.mentormenteesystem.R;
import com.dev.mentormenteesystem.mentor.model.ApprovedMentorModel;
import com.dev.mentormenteesystem.mentor.model.MentorPendingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MentorRequestAdapter extends RecyclerView.Adapter<MentorRequestAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<MentorPendingModel> list;
    FirebaseAuth auth;
    DatabaseReference dbReferencePending;
    DatabaseReference dbReferenceApproved;
    ProgressDialog progressDialog;

    public MentorRequestAdapter(Context mContext, ArrayList<MentorPendingModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_menot_request, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MentorPendingModel data = list.get(position);
        holder.tvFullName.setText(data.getFullName());
        holder.tvStudentID.setText(data.getStudentID());
        holder.tvGroup.setText(data.getGroup());
        holder.tvSemester.setText(data.getSemester());
        holder.tvSubject1.setText(data.getCode1());
        holder.tvSubject2.setText(data.getCode2());
        holder.tvSubject3.setText(data.getCode3());
        holder.tvSubject4.setText(data.getCode4());
        holder.tvGrade1.setText(data.getGrade1());
        holder.tvGrade2.setText(data.getGrade2());
        holder.tvGrade3.setText(data.getGrade3());
        holder.tvGrade4.setText(data.getGrade4());

        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApproveMethod(data.getuID());
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRejectMethod(data.getuID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFullName, tvStudentID, tvGroup, tvSemester, tvSubject1, tvSubject2, tvSubject3, tvSubject4;
        TextView tvGrade1, tvGrade2, tvGrade3, tvGrade4;
        AppCompatButton btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvStudentID = itemView.findViewById(R.id.tvStudentID);
            tvGroup = itemView.findViewById(R.id.tvGroup);
            tvSemester = itemView.findViewById(R.id.tvSemester);
            tvSubject1 = itemView.findViewById(R.id.tvSubject1);
            tvSubject2 = itemView.findViewById(R.id.tvSubject2);
            tvSubject3 = itemView.findViewById(R.id.tvSubject3);
            tvSubject4 = itemView.findViewById(R.id.tvSubject4);
            tvGrade1 = itemView.findViewById(R.id.tvGrade1);
            tvGrade2 = itemView.findViewById(R.id.tvGrade2);
            tvGrade3 = itemView.findViewById(R.id.tvGrade3);
            tvGrade4 = itemView.findViewById(R.id.tvGrade4);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    private void callApproveMethod(String uID){
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Going Good...");
        progressDialog.setMessage("It takes Just a few Seconds... ");
        progressDialog.setIcon(R.drawable.happy);
        progressDialog.setCancelable(false);
        progressDialog.show();
        auth = FirebaseAuth.getInstance();
        dbReferencePending = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("PendingMentors").child(uID);
        dbReferenceApproved = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("ApprovedMentors");
        ApprovedMentorModel model = new ApprovedMentorModel(uID);
        String pushID = dbReferenceApproved.push().getKey();
        dbReferenceApproved.child(pushID).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dbReferencePending.removeValue();
                progressDialog.dismiss();
                notifyDataSetChanged();
                Toast.makeText(mContext, "Successfully Approved", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "Successfully Approved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callRejectMethod(String uID){
        auth = FirebaseAuth.getInstance();
        dbReferencePending = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("PendingMentors").child(uID);
        dbReferencePending.removeValue();
        notifyDataSetChanged();
    }

}
