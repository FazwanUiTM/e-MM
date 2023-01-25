package com.dev.mentormenteesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dev.mentormenteesystem.auth.LoginActivity;
import com.dev.mentormenteesystem.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        binding.btnMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToLoginActivity("Mentor");
            }
        });

        binding.btnMentee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToLoginActivity("Mentee");
            }
        });

        binding.btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToLoginActivity("Admin");
            }
        });

    }

    private void moveToLoginActivity(String value){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("checkLogin", value);
        startActivity(intent);
    }
}