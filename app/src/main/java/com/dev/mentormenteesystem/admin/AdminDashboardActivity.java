package com.dev.mentormenteesystem.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dev.mentormenteesystem.R;
import com.dev.mentormenteesystem.databinding.ActivityAdminDashboardBinding;
import com.dev.mentormenteesystem.mentor.AddSubjectActivity;
import com.dev.mentormenteesystem.mentor.MentorDashboardActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    ActivityAdminDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_dashboard);

        binding.btnMentorRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this, MentorRequestsActivity.class));
            }
        });

    }
}