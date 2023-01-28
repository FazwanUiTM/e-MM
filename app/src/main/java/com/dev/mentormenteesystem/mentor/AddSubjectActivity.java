package com.dev.mentormenteesystem.mentor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dev.mentormenteesystem.R;
import com.dev.mentormenteesystem.databinding.ActivityAddSubjectBinding;
import com.dev.mentormenteesystem.mentor.model.MentorPendingModel;
import com.dev.mentormenteesystem.mentor.model.SubjectsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddSubjectActivity extends AppCompatActivity {

    ActivityAddSubjectBinding  binding;
    String getFullName = "";
    String getImage = "";
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    DatabaseReference dbReference;
    String strSubjectName, strSubjectCode, strDayTime, strStudentLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_subject);

        getFullName = getIntent().getStringExtra("fullName");
        getImage = getIntent().getStringExtra("getImage");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Going Good...");
        progressDialog.setMessage("It takes Just a few Seconds... ");
        progressDialog.setIcon(R.drawable.happy);
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("MentorsSubjectData");


        binding.btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSubjectName = binding.etSubject.getText().toString();
                strSubjectCode = binding.etCode.getText().toString();
                strDayTime = binding.etDateTime.getText().toString();
                strStudentLimit = binding.etLimit.getText().toString();

                if (strSubjectName.isEmpty()){
                    showToast("Please Enter Subject Name");
                }else if (strSubjectCode.isEmpty()){
                    showToast("Please Enter Subject Code");
                }else if (strDayTime.isEmpty()){
                    showToast("Please Enter Day and Time");
                }else if (strStudentLimit.isEmpty()){
                    showToast("Please Enter Subject Limit");
                }else{
                    progressDialog.show();

                    SubjectsModel model = new SubjectsModel(getFullName, strSubjectName, strSubjectCode, strDayTime, strStudentLimit, getImage ,auth.getCurrentUser().getUid());
                    String pushID = dbReference.push().getKey();
                    dbReference.child(pushID).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            showToast("Successfully Added");
                            finish();
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}