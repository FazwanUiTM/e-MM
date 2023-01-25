package com.dev.mentormenteesystem.mentor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.mentormenteesystem.R;
import com.dev.mentormenteesystem.databinding.ActivityMentorProfileBinding;
import com.google.android.gms.common.internal.Objects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MentorProfileActivity extends AppCompatActivity {

    ActivityMentorProfileBinding binding;
    String checkProfile = "";
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    DatabaseReference dbReference;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mentor_profile);

        checkProfile = getIntent().getStringExtra("checkProfile");
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        if (Objects.equal(checkProfile, "Mentee")){
            binding.tvLabel.setText("Mentee Profile");
            dbReference = FirebaseDatabase.getInstance(
                    "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("RegistrationData").child("MenteeRegistrationData").child(auth.getCurrentUser().getUid());

        }else if (Objects.equal(checkProfile, "Mentor")){
            binding.tvLabel.setText("Mentor Profile");
            dbReference = FirebaseDatabase.getInstance(
                    "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("RegistrationData").child("MentorRegistrationData").child(auth.getCurrentUser().getUid());
        }

        showData();

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
                String getImage = snapshot.child("image").getValue(String.class);
                Glide.with(binding.ivProfile).load(getImage).into(binding.ivProfile);
                binding.tvPerson.setText(snapshot.child("fullName").getValue(String.class));
                binding.tvGroup.setText(snapshot.child("group").getValue(String.class));
                binding.tvSemester.setText(snapshot.child("semester").getValue(String.class));
                binding.tvPhone.setText(snapshot.child("phoneNumber").getValue(String.class));

                if (Objects.equal(checkProfile, "Mentor")){
                    binding.tvStudentID.setText(snapshot.child("studentID").getValue(String.class));
                }else{
                    binding.tvStudentID.setVisibility(View.GONE);
                }

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