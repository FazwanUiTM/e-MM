package com.dev.mentormenteesystem.mentee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dev.mentormenteesystem.R;
import com.dev.mentormenteesystem.databinding.ActivityStatusRequestBinding;
import com.dev.mentormenteesystem.mentee.adaptor.RequestStatusAdapter;
import com.dev.mentormenteesystem.mentor.MenteeRequestsActivity;
import com.dev.mentormenteesystem.mentor.adapter.MenteeRequestToMentorAdapter;
import com.dev.mentormenteesystem.mentor.model.MenteeRequestModel;
import com.dev.mentormenteesystem.mentor.model.MenteeRequestStatusModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatusRequestActivity extends AppCompatActivity {

    ActivityStatusRequestBinding binding;
    FirebaseAuth auth;
    DatabaseReference dbReference;
    ProgressDialog progressDialog;
    RequestStatusAdapter adapter;
    ArrayList<MenteeRequestStatusModel> listRequestStatus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_status_request);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Going Good...");
        progressDialog.setMessage("It takes Just a few Seconds... ");
        progressDialog.setIcon(R.drawable.happy);
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();

        showData();

    }

    private void showData() {
        progressDialog.show();
        dbReference = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("MenteeRequestsStatus").child(auth.getCurrentUser().getUid());
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.rvItems.setVisibility(View.VISIBLE);
                    binding.tvNoDataFound.setVisibility(View.GONE);
                    listRequestStatus.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        MenteeRequestStatusModel model = ds.getValue(MenteeRequestStatusModel.class);
                        listRequestStatus.add(model);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StatusRequestActivity.this, LinearLayoutManager.VERTICAL, false);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    binding.rvItems.setLayoutManager(linearLayoutManager);
                    adapter = new RequestStatusAdapter(StatusRequestActivity.this, listRequestStatus);
                    binding.rvItems.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                }else{
                    binding.rvItems.setVisibility(View.GONE);
                    binding.tvNoDataFound.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(StatusRequestActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}