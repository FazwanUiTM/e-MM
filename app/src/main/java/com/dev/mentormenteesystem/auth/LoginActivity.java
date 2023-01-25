package com.dev.mentormenteesystem.auth;

import androidx.activity.result.ActivityResultCaller;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.dev.mentormenteesystem.MainActivity;
import com.dev.mentormenteesystem.R;
import com.dev.mentormenteesystem.admin.AdminDashboardActivity;
import com.dev.mentormenteesystem.databinding.ActivityLoginBinding;
import com.dev.mentormenteesystem.mentee.MenteeDashboardActivity;
import com.dev.mentormenteesystem.mentor.MentorDashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    String checkLogin = "";
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    DatabaseReference dbReference;
    DatabaseReference dbReference2;
    String strEmail, strPassword;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        checkLogin = getIntent().getStringExtra("checkLogin");

        if (Objects.equals(checkLogin, "Mentor")) {
            binding.tvLabel.setText("Mentor Login");
        } else if (Objects.equals(checkLogin, "Mentee")) {
            binding.tvLabel.setText("Mentee Login");
        } else if (Objects.equals(checkLogin, "Admin")) {
            binding.tvLabel.setText("Admin Login");
            binding.rlSignUp.setVisibility(View.GONE);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Going Good...");
        progressDialog.setMessage("It takes Just a few Seconds... ");
        progressDialog.setIcon(R.drawable.happy);
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();

        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(checkLogin, "Mentor")) {
                    moveToRegisterActivity("Mentor");
                } else if (Objects.equals(checkLogin, "Mentee")) {
                    moveToRegisterActivity("Mentee");
                }
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strEmail = binding.etEmail.getText().toString();
                strPassword = binding.etPassword.getText().toString();

                if (strEmail.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (!(Patterns.EMAIL_ADDRESS).matcher(strEmail).matches()) {
                    Toast.makeText(LoginActivity.this, "Please enter email in correct format", Toast.LENGTH_SHORT).show();
                } else if (strPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (Objects.equals(checkLogin, "Mentor")) {
                                    dbReference = FirebaseDatabase.getInstance(
                                            "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("RegistrationData").child("MentorRegistrationData");
                                    checkMentor();
                                } else if (Objects.equals(checkLogin, "Mentee")) {
                                    dbReference = FirebaseDatabase.getInstance(
                                            "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("RegistrationData").child("MenteeRegistrationData");
                                    checkMentee();
                                } else if (Objects.equals(checkLogin, "Admin")) {
                                    if (Objects.equals(strEmail, "admin@gmail.com") && Objects.equals(strPassword, "admin@123")) {
                                        progressDialog.dismiss();
                                        startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                                        finish();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Invalid UserName or Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void checkMentor() {
        dbReference2 = dbReference.child(auth.getCurrentUser().getUid());
        dbReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String getStrCategory = dataSnapshot.child("category").getValue(String.class);
                if (Objects.equals(getStrCategory, "Mentor")) {
                    showToast("Successfully Login");
                    progressDialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, MentorDashboardActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    showToast("Invalid UserName or Password");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                showToast(databaseError.getMessage());

            }
        });
    }

    private void checkMentee(){
        dbReference2 = dbReference.child(auth.getCurrentUser().getUid());
        dbReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String getStrCategory = dataSnapshot.child("category").getValue(String.class);
                if (Objects.equals(getStrCategory, "Mentee")) {
                    showToast("Successfully Login");
                    progressDialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, MenteeDashboardActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    showToast("Invalid UserName or Password");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                showToast(databaseError.getMessage());

            }
        });
    }

    private void moveToRegisterActivity(String value) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.putExtra("checkRegister", value);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}