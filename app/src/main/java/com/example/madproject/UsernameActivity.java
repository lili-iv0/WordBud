package com.example.madproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UsernameActivity extends AppCompatActivity {
    EditText username;
    Button usernameBtn;
    Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        username = findViewById(R.id.username);
        usernameBtn = findViewById(R.id.usernameBtn);

        initialize();
    }

    private void initialize() {
        usernameBtn.setOnClickListener(v -> {
            String name = username.getText().toString().trim();

            //if empty no entry
            if (name.isEmpty()) {
                new AlertDialog.Builder(c)
                        .setTitle("Oops!")
                        .setMessage("Please enter your username before continuing.")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
                return;
            }

            //pass username sa homeAct so mag show dun
            Intent intent = new Intent(c, HomeActivity.class);
            intent.putExtra("username", name);

            // Get credentials from Register/Login and pass them to HomeActivity
            Intent receivedIntent = getIntent();
            if (receivedIntent != null) {
                intent.putExtra("registeredEmail", receivedIntent.getStringExtra("registeredEmail"));
                intent.putExtra("registeredPassword", receivedIntent.getStringExtra("registeredPassword"));
            }

            startActivity(intent);
            finish(); // Add this to prevent going back to UsernameActivity
        });
    }
}


