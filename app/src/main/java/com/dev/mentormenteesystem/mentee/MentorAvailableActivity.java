package com.dev.mentormenteesystem.mentee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dev.mentormenteesystem.R;
import com.dev.mentormenteesystem.admin.MentorRequestsActivity;
import com.dev.mentormenteesystem.admin.adaptor.MentorRequestAdapter;
import com.dev.mentormenteesystem.databinding.ActivityMentorAvailableBinding;
import com.dev.mentormenteesystem.mentee.adaptor.AvailableMentorAdapter;
import com.dev.mentormenteesystem.mentor.model.MenteeRequestModel;
import com.dev.mentormenteesystem.mentor.model.MenteeRequestStatusModel;
import com.dev.mentormenteesystem.mentor.model.MentorPendingModel;
import com.dev.mentormenteesystem.mentor.model.SubjectsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MentorAvailableActivity extends AppCompatActivity implements AvailableMentorAdapter.OnShareClickedListener{

    ActivityMentorAvailableBinding binding;
    FirebaseAuth auth;
    DatabaseReference dbReference;
    DatabaseReference dbReferenceRequests;
    DatabaseReference dbReferenceMenteeRequestsStatus;
    ProgressDialog progressDialog;
    AvailableMentorAdapter adapter;
    ArrayList<SubjectsModel> listMentors = new ArrayList<>();
    String getFullName, getGroup, getSemester;
    String pushIDFirst;
    String pushIDSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mentor_available);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Going Good...");
        progressDialog.setMessage("It takes Just a few Seconds... ");
        progressDialog.setIcon(R.drawable.happy);
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();

        getFullName = getIntent().getStringExtra("fullName");
        getSemester = getIntent().getStringExtra("semester");
        getGroup = getIntent().getStringExtra("group");

        showData();

    }

    private void showData() {
        progressDialog.show();
        dbReference = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("MentorsSubjectData");

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.rvItems.setVisibility(View.VISIBLE);
                    binding.tvNoDataFound.setVisibility(View.GONE);
                    listMentors.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        SubjectsModel model = ds.getValue(SubjectsModel.class);
                        listMentors.add(model);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MentorAvailableActivity.this, LinearLayoutManager.VERTICAL, false);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    binding.rvItems.setLayoutManager(linearLayoutManager);
                    adapter = new AvailableMentorAdapter(MentorAvailableActivity.this, listMentors);
                    adapter.setOnShareClickedListener(MentorAvailableActivity.this);
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
                Toast.makeText(MentorAvailableActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<SubjectsModel> filteredlist = new ArrayList<SubjectsModel>();
        // running a for loop to compare elements.
        for (SubjectsModel item : listMentors) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getSubjectName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
                adapter.notifyDataSetChanged();
            }
        }
        if (filteredlist.isEmpty()) {
            adapter.notifyDataSetChanged();
        } else {
            adapter.filterList(filteredlist);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClicked(SubjectsModel data, int position) {
        ProgressDialog mPD;
        mPD = new ProgressDialog(this);
        mPD.setTitle("Going Good...");
        mPD.setMessage("It takes Just a few Seconds... ");
        mPD.setIcon(R.drawable.happy);
        mPD.setCancelable(false);
        mPD.show();
        dbReferenceMenteeRequestsStatus = FirebaseDatabase.getInstance(
                "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("MenteeRequestsStatus").child(auth.getCurrentUser().getUid());
        pushIDFirst = dbReferenceMenteeRequestsStatus.push().getKey();
        MenteeRequestStatusModel statusModel = new MenteeRequestStatusModel(data.getSubjectCode(),"Pending");
        dbReferenceMenteeRequestsStatus.child(pushIDFirst).setValue(statusModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dbReferenceRequests = FirebaseDatabase.getInstance(
                        "https://mentor-mentee-system-fe125-default-rtdb.firebaseio.com").getReference("MenteeRequestsToMentors").child(data.getuID());
                pushIDSecond = dbReferenceRequests.push().getKey();
                MenteeRequestModel requestModel = new MenteeRequestModel(getFullName, getGroup, getSemester, data.getSubjectName(),
                        auth.getCurrentUser().getUid(), pushIDFirst, pushIDSecond);
                dbReferenceRequests.child(pushIDSecond).setValue(requestModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (mPD.isShowing()) {
                            mPD.dismiss();
                        }
                        showToast("Request Sent Successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mPD.isShowing()) {
                            mPD.dismiss();
                        }
                        showToast(e.getLocalizedMessage());
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mPD.dismiss();
                showToast(e.getLocalizedMessage());
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}