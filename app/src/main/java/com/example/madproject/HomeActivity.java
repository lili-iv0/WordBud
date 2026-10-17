package com.example.madproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends Navigation {

    ImageView wordFrenzyView, mindFlipView, vocabVistaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get credentials from intent
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("username");
            registeredEmail = intent.getStringExtra("registeredEmail");
            registeredPassword = intent.getStringExtra("registeredPassword");
        }

        // assign activity cards
        wordFrenzyView = findViewById(R.id.wordFrenzyView);
        mindFlipView = findViewById(R.id.mindFlipView);
        vocabVistaView = findViewById(R.id.vocabVistaView);

        // assign nav buttons
        homeNavBtn = findViewById(R.id.homeNavBtn);
        addWordNavBtn = findViewById(R.id.addWordNavBtn);
        wordListNavBtn = findViewById(R.id.wordListNavBtn);
        profileNavBtn = findViewById(R.id.profileNavbtn);

        setupNavigation(); // sa Navigation act

        // display username
        TextView welcomeText = findViewById(R.id.usernamePlaceHolder);
        if (username != null && !username.isEmpty()) {
            welcomeText.setText(username);
        }

        initialize();
    }

    private void initialize() {
        wordFrenzyView.setOnClickListener(v -> {
            Intent intent = new Intent(c, WordFrenzyActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("registeredEmail", registeredEmail);
            intent.putExtra("registeredPassword", registeredPassword);
            startActivity(intent);
        });

        mindFlipView.setOnClickListener(v -> {
            Intent intent = new Intent(c, MindFlipActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("registeredEmail", registeredEmail);
            intent.putExtra("registeredPassword", registeredPassword);
            startActivity(intent);
        });

        vocabVistaView.setOnClickListener(v -> {
            Intent intent = new Intent(c, VocabVistaActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("registeredEmail", registeredEmail);
            intent.putExtra("registeredPassword", registeredPassword);
            startActivity(intent);
        });
    }
}