package com.dev.mentormenteesystem.mentee.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dev.mentormenteesystem.R;
import com.dev.mentormenteesystem.mentor.model.MenteeRequestStatusModel;
import com.dev.mentormenteesystem.mentor.model.SubjectsModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestStatusAdapter extends RecyclerView.Adapter<RequestStatusAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<MenteeRequestStatusModel> list;

    public RequestStatusAdapter(Context mContext, ArrayList<MenteeRequestStatusModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MenteeRequestStatusModel data = list.get(position);
        holder.tvSubjectCode.setText(data.getSubjectCode());
        holder.tvStatus.setText(data.getStatus());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectCode, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectCode = itemView.findViewById(R.id.tvSubjectCode);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

}
