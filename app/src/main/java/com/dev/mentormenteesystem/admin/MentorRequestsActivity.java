package com.dev.mentormenteesystem.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dev.mentormenteesystem.R;
import com.dev.mentormenteesystem.admin.adaptor.MentorRequestAdapter;
import com.dev.mentormenteesystem.databinding.ActivityMentorRequestsBinding;
import com.dev.mentormenteesystem.mentor.model.MentorPendingModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MentorRequestsActivity extends AppCompatActivity {

    ActivityMentorRequestsBinding binding;
    FirebaseAuth auth;
    DatabaseReference dbReference;
    DatabaseReference dbReferencePending;
    ProgressDialog progressDialog;
    MentorRequestAdapter adapter;
    ArrayList<MentorPendingModel> listPending = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mentor_requests);


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
        dbReferencePending = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("PendingMentors");

        dbReferencePending.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.rvItems.setVisibility(View.VISIBLE);
                    binding.tvNoDataFound.setVisibility(View.GONE);
                    listPending.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        MentorPendingModel model = ds.getValue(MentorPendingModel.class);
                        listPending.add(model);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MentorRequestsActivity.this, LinearLayoutManager.VERTICAL, false);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    binding.rvItems.setLayoutManager(linearLayoutManager);
                    adapter = new MentorRequestAdapter(MentorRequestsActivity.this, listPending);
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
                Toast.makeText(MentorRequestsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}