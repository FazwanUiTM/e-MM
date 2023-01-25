package com.dev.mentormenteesystem.mentor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.mentormenteesystem.R;
import com.dev.mentormenteesystem.databinding.ActivityMentorDashboardBinding;
import com.dev.mentormenteesystem.mentee.MenteeDashboardActivity;
import com.dev.mentormenteesystem.mentee.StatusRequestActivity;
import com.dev.mentormenteesystem.mentor.model.ApprovedMentorModel;
import com.dev.mentormenteesystem.mentor.model.MentorPendingModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MentorDashboardActivity extends AppCompatActivity {

    ActivityMentorDashboardBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    DatabaseReference dbReference;
    DatabaseReference dbReferenceApproved;
    String fullName, getImage;
    ArrayList<String> listApproved = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mentor_dashboard);

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("RegistrationData").child("MentorRegistrationData").child(auth.getCurrentUser().getUid());
        dbReferenceApproved = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("ApprovedMentors");

        showData();

        binding.btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listApproved.contains(auth.getCurrentUser().getUid())){
                    Intent intent = new Intent(MentorDashboardActivity.this, AddSubjectActivity.class);
                    intent.putExtra("fullName", fullName);
                    intent.putExtra("getImage", getImage);
                    startActivity(intent);
                }else{
                    Toast.makeText(MentorDashboardActivity.this, "Admin has not Approved yet", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.btnMenteeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listApproved.contains(auth.getCurrentUser().getUid())){
                    startActivity(new Intent(MentorDashboardActivity.this, MenteeRequestsActivity.class));
                }else{
                    Toast.makeText(MentorDashboardActivity.this, "Admin has not Approved yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MentorDashboardActivity.this, MentorProfileActivity.class);
                intent.putExtra("checkProfile", "Mentor");
                startActivity(intent);
            }
        });
    }

    private void showData() {
        //Show Progress Dialog
        progressDialog.setTitle("Going Good...");
        progressDialog.setMessage("It takes Just a few Seconds... ");
        progressDialog.setIcon(R.drawable.happy);
        progressDialog.setCancelable(false);
        progressDialog.show();
        //Add Listener & Show Values
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullName =  snapshot.child("fullName").getValue(String.class);
                binding.tvUserName.setText(snapshot.child("userName").getValue(String.class));
                getImage = snapshot.child("image").getValue(String.class);
                Glide.with(binding.ivProfile).load(getImage).into(binding.ivProfile);

                dbReferenceApproved.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        if (snapshot.exists()){
                            for (DataSnapshot ds : snapshot.getChildren()){
                                ApprovedMentorModel model = ds.getValue(ApprovedMentorModel.class);
                                listApproved.add(model.getuID());
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}