package com.dev.mentormenteesystem.mentee;

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
import com.dev.mentormenteesystem.databinding.ActivityMenteeDashboardBinding;
import com.dev.mentormenteesystem.mentor.MentorProfileActivity;
import com.dev.mentormenteesystem.mentor.model.ApprovedMentorModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenteeDashboardActivity extends AppCompatActivity {

    ActivityMenteeDashboardBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    DatabaseReference dbReference;
    String strFullName, strGroup, strSemester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mentee_dashboard);

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("RegistrationData").child("MenteeRegistrationData").child(auth.getCurrentUser().getUid());

        showData();


        binding.btnMentorAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenteeDashboardActivity.this, MentorAvailableActivity.class);
                intent.putExtra("fullName", strFullName);
                intent.putExtra("group", strGroup);
                intent.putExtra("semester", strSemester);
                startActivity(intent);

            }
        });

        binding.btnStatusRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenteeDashboardActivity.this, StatusRequestActivity.class));
            }
        });

        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenteeDashboardActivity.this, MentorProfileActivity.class);
                intent.putExtra("checkProfile","Mentee");
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
                binding.tvUserName.setText(snapshot.child("userName").getValue(String.class));
                String getImage = snapshot.child("image").getValue(String.class);
                strFullName = snapshot.child("fullName").getValue(String.class);
                strGroup = snapshot.child("group").getValue(String.class);
                strSemester = snapshot.child("semester").getValue(String.class);
                Glide.with(binding.ivProfile).load(getImage).into(binding.ivProfile);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}