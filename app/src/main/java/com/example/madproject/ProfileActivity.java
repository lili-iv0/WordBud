package com.example.madproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends Navigation {
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // assign nav buttons
        homeNavBtn = findViewById(R.id.homeNavBtn);
        addWordNavBtn = findViewById(R.id.addWordNavBtn);
        wordListNavBtn = findViewById(R.id.wordListNavBtn);
        profileNavBtn = findViewById(R.id.profileNavbtn);
        logout = findViewById(R.id.logout);

        setupNavigation(); // sa Navigation act

        // Get username and credentials from intent
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("username");
            registeredEmail = intent.getStringExtra("registeredEmail");
            registeredPassword = intent.getStringExtra("registeredPassword");
        }

        // display username
        TextView welcomeText = findViewById(R.id.usernamePlaceHolder);
        if (username != null && !username.isEmpty()) {
            welcomeText.setText(username);
        }

        initialize();
    }

    private void initialize() {
        logout.setOnClickListener(v -> {
            // Add confirmation dialog for logout
            new AlertDialog.Builder(c)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Intent intent = new Intent(c, LoginActivity.class);
                        // Pass credentials AND username back to LoginActivity
                        intent.putExtra("registeredEmail", registeredEmail);
                        intent.putExtra("registeredPassword", registeredPassword);
                        intent.putExtra("username", username); // Pass the username
                        startActivity(intent);
                        finish(); // Close current activity
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}