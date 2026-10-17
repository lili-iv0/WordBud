package com.example.madproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public abstract class Navigation extends AppCompatActivity {

    protected Context c;
    protected String username;
    protected String registeredEmail;
    protected String registeredPassword;
    protected Button homeNavBtn, addWordNavBtn, wordListNavBtn, profileNavBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;
    }

    protected void setupNavigation() {
        // get username and credentials from current intent
        if (getIntent() != null) {
            username = getIntent().getStringExtra("username");
            registeredEmail = getIntent().getStringExtra("registeredEmail");
            registeredPassword = getIntent().getStringExtra("registeredPassword");
        }

        // navigation listeners
        if (homeNavBtn != null) {
            homeNavBtn.setOnClickListener(v -> navigate(HomeActivity.class));
        }
        if (addWordNavBtn != null) {
            addWordNavBtn.setOnClickListener(v -> navigate(AddWordActivity.class));
        }
        if (wordListNavBtn != null) {
            wordListNavBtn.setOnClickListener(v -> navigate(WordListActivity.class));
        }
        if (profileNavBtn != null) {
            profileNavBtn.setOnClickListener(v -> navigate(ProfileActivity.class));
        }
    }

    private void navigate(Class<?> target) {
        if (this.getClass() != target) { // prevents reopening same activity
            Intent intent = new Intent(c, target);
            intent.putExtra("username", username);
            intent.putExtra("registeredEmail", registeredEmail);
            intent.putExtra("registeredPassword", registeredPassword);
            startActivity(intent);
            finish();
        }
    }
}