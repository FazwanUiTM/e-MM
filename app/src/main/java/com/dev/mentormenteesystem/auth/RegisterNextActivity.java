    package com.dev.mentormenteesystem.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.dev.mentormenteesystem.R;
import com.dev.mentormenteesystem.auth.model.MentorRegisterModel;
import com.dev.mentormenteesystem.databinding.ActivityRegisterNextBinding;
import com.dev.mentormenteesystem.mentor.MentorDashboardActivity;
import com.dev.mentormenteesystem.mentor.model.MentorPendingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

    public class RegisterNextActivity extends AppCompatActivity {

        ActivityRegisterNextBinding binding;
        ProgressDialog progressDialog;
        FirebaseAuth auth;
        DatabaseReference dbReference;
        DatabaseReference dbReferencePending;
        StorageReference storageRef, imageReference;
        String imageUri;
        String strFullName, strUserName, strEmail, strPassword, strSemester, strGroup, strPhoneNumber, strImage;
        String strStudentID, strCode1, strCode2, strCode3, strCode4, strGrade1, strGrade2, strGrade3, strGrade4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_next);

        strFullName = getIntent().getStringExtra("fullName");
        strUserName = getIntent().getStringExtra("userName");
        strEmail = getIntent().getStringExtra("email");
        strPassword = getIntent().getStringExtra("password");
        strSemester = getIntent().getStringExtra("semester");
        strGroup = getIntent().getStringExtra("group");
        strPhoneNumber = getIntent().getStringExtra("phone");
        strImage = getIntent().getStringExtra("image");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Going Good...");
        progressDialog.setMessage("It takes Just a few Seconds... ");
        progressDialog.setIcon(R.drawable.happy);
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("RegistrationData").child("MentorRegistrationData");
        dbReferencePending = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("PendingMentors");

        storageRef = FirebaseStorage.getInstance().getReference("ProfileImages");

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()){
                    progressDialog.show();

                    auth.createUserWithEmailAndPassword(strEmail, strPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            imageReference = storageRef.child(Uri.parse(strImage).getLastPathSegment());
                            imageReference.putFile(Uri.parse(strImage)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            String uploadedImgURL = uri.toString();

                                            MentorRegisterModel model = new MentorRegisterModel(strFullName, strUserName, strEmail, strPassword, strSemester,
                                                    strGroup, strPhoneNumber, strStudentID, strCode1, strCode2, strCode3, strCode4, strGrade1, strGrade2,
                                                    strGrade3, strGrade4, "Mentor", uploadedImgURL);
                                            dbReference.child(auth.getCurrentUser().getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    MentorPendingModel model = new MentorPendingModel(auth.getCurrentUser().getUid(), strFullName, strStudentID,
                                                            strGroup, strSemester, strCode1, strCode2, strCode3, strCode4, strGrade1, strGrade2,
                                                            strGrade3, strGrade4);

                                                    dbReferencePending.child(auth.getCurrentUser().getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            progressDialog.dismiss();
                                                            showToast("Successfully Registered as a Mentor");
                                                            startActivity(new Intent(RegisterNextActivity.this, MentorDashboardActivity.class));
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            showToast(e.getLocalizedMessage());
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    showToast(e.getLocalizedMessage());
                                                }
                                            });

                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    showToast(e.getLocalizedMessage());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            showToast(e.getLocalizedMessage());
                        }
                    });
                }
            }
        });

    }

        public boolean checkValidation() {
            strStudentID = binding.etStudentID.getText().toString().trim();
            strCode1 = binding.etCodeSubject1.getText().toString().trim();
            strCode2 = binding.etCodeSubject2.getText().toString();
            strCode3 = binding.etCodeSubject3.getText().toString().trim();
            strCode4 = binding.etCodeSubject4.getText().toString().trim();
            strGrade1 = binding.etGrade1.getText().toString().trim();
            strGrade2 = binding.etGrade2.getText().toString().trim();
            strGrade3 = binding.etGrade3.getText().toString().trim();
            strGrade4 = binding.etGrade4.getText().toString().trim();

            if (TextUtils.isEmpty(strStudentID)) {
                showToast("Please Enter Student ID");
                return false;
            } else if (TextUtils.isEmpty(strCode1) || TextUtils.isEmpty(strCode2) || TextUtils.isEmpty(strCode3) || TextUtils.isEmpty(strCode4)) {
                showToast("All Code Subject fields must be filled");
                return false;
            }else if (TextUtils.isEmpty(strGrade1) || TextUtils.isEmpty(strGrade2) || TextUtils.isEmpty(strGrade3) || TextUtils.isEmpty(strGrade4)) {
                showToast("All Grade fields must be filled");
                return false;
            }

            return true;
        }

        private void showToast(String message) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

}