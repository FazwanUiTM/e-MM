package com.dev.mentormenteesystem.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.dev.mentormenteesystem.R;
import com.dev.mentormenteesystem.auth.model.MenteeRegisterModel;
import com.dev.mentormenteesystem.databinding.ActivityLoginBinding;
import com.dev.mentormenteesystem.databinding.ActivityRegisterBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
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

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    String checkRegister = "";
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    DatabaseReference dbReference;
    StorageReference storageRef, imageReference;
    String imageUri;
    int PICK_IMAGE_GALLERY = 124;
    String strFullName, strUserName, strEmail, strPassword, strConPassword, strSemester, strGroup, strPhoneNumber;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        checkRegister = getIntent().getStringExtra("checkRegister");

        if (Objects.equals(checkRegister,"Mentor")){
            binding.tvLabel.setText("Mentor Register");
            binding.btnRegister.setText("Next");
        }else if (Objects.equals(checkRegister,"Mentee")){
            binding.tvLabel.setText("Mentee Register");
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Going Good...");
        progressDialog.setMessage("It takes Just a few Seconds... ");
        progressDialog.setIcon(R.drawable.happy);
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("RegistrationData").child("MenteeRegistrationData");
        storageRef = FirebaseStorage.getInstance().getReference("ProfileImages");

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             onBackPressed();
            }
        });

        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(RegisterActivity.this)
                        .compress(512)
                        .maxResultSize(512, 512)
                        .start();

                Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidation()){

                    if (Objects.equals(checkRegister,"Mentor")){

                        Intent intent = new Intent(RegisterActivity.this, RegisterNextActivity.class);
                        intent.putExtra("fullName", strFullName);
                        intent.putExtra("userName", strUserName);
                        intent.putExtra("email", strEmail);
                        intent.putExtra("password", strPassword);
                        intent.putExtra("semester", strSemester);
                        intent.putExtra("group", strGroup);
                        intent.putExtra("phone", strPhoneNumber);
                        intent.putExtra("image", imageUri);
                        startActivity(intent);

                    }else{
                        progressDialog.show();

                        auth.createUserWithEmailAndPassword(strEmail, strPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                imageReference = storageRef.child(Uri.parse(imageUri).getLastPathSegment());
                                imageReference.putFile(Uri.parse(imageUri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                String uploadedImgURL = uri.toString();

                                                MenteeRegisterModel model = new MenteeRegisterModel(strFullName, strUserName, strEmail, strPassword, strSemester, strGroup, strPhoneNumber,"Mentee", uploadedImgURL);
                                                dbReference.child(auth.getCurrentUser().getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressDialog.dismiss();
                                                        showToast("Successfully Registered as a Mentee");
                                                        startActivity(new Intent(RegisterActivity.this, RegisterNextActivity.class));
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
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK) {
            //Getting Gallery Image uri
            Uri uriImage = data.getData();
            try {
                binding.ivProfile.setImageURI(uriImage);
                imageUri = uriImage.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkValidation() {
        strFullName = binding.etFullName.getText().toString().trim();
        strUserName = binding.etUserName.getText().toString().trim();
        strEmail = binding.etEmail.getText().toString();
        strPassword = binding.etPassword.getText().toString().trim();
        strConPassword = binding.etConPassword.getText().toString().trim();
        strSemester = binding.etSemester.getText().toString().trim();
        strGroup = binding.etGroup.getText().toString().trim();
        strPhoneNumber = binding.etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(strFullName)) {
            showToast("Please Enter Full Name");
            return false;
        } else if (TextUtils.isEmpty(strUserName)) {
            showToast("Please Enter User Name");
            return false;
        }else if (TextUtils.isEmpty(strEmail)) {
            showToast("Please Enter Email");
            return false;
        } else if (!(Patterns.EMAIL_ADDRESS).matcher(strEmail).matches()) {
            showToast("Please Enter Email in Correct Format");
            return false;
        } else if (TextUtils.isEmpty(strPassword)) {
            showToast("Please Write Password");
            return false;
        } else if (TextUtils.isEmpty(strConPassword)) {
            showToast("Please Write Confirm Password");
            return false;
        }else if (!Objects.equals(strPassword, strConPassword)) {
            showToast("Password should must be matched");
            return false;
        } else if (TextUtils.isEmpty(strSemester)) {
            showToast("Please Write Semester");
            return false;
        }else if (TextUtils.isEmpty(strGroup)) {
            showToast("Please Write Group");
            return false;
        }else if (TextUtils.isEmpty(strPhoneNumber)) {
            showToast("Please Write Phone Number");
            return false;
        }else if (TextUtils.isEmpty(imageUri)) {
            showToast("Please Select Profile Picture");
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}