package com.example.madproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    TextView registerText;
    EditText emailLogin, passwordLogin;
    Context c = this;

    String registeredEmail;
    String registeredPassword;
    String existingUsername; // Add this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //di mag oopen act pag walang gantuuu
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        loginBtn = findViewById(R.id.loginBtn);
        registerText = findViewById(R.id.registerText);

        //makuha yung sa register na email & pass
        Intent intent = getIntent();
        registeredEmail = intent.getStringExtra("registeredEmail");
        registeredPassword = intent.getStringExtra("registeredPassword");
        existingUsername = intent.getStringExtra("username"); // Get username if exists

        initialize();
    }

    private void initialize() {
        loginBtn.setOnClickListener(v -> {
            String email = emailLogin.getText().toString().trim();
            String password = passwordLogin.getText().toString().trim();

            //alert if may empty
            if (email.isEmpty() || password.isEmpty()) {
                new AlertDialog.Builder(c)
                        .setTitle("Login Failed")
                        .setMessage("Please fill in all fields!")
                        .setPositiveButton("OK", null)
                        .show();

                //alert pag di no existing acc
            } else if (registeredEmail == null || registeredPassword == null) {
                new AlertDialog.Builder(c)
                        .setTitle("Login Failed")
                        .setMessage("No registered account found. Please register first!")
                        .setPositiveButton("OK", null)
                        .show();

                //navigate to usernameAct OR homeAct
            } else if (email.equals(registeredEmail) && password.equals(registeredPassword)) {

                if (existingUsername != null && !existingUsername.isEmpty()) {
                    // If username exists (2nd time login), show success alert then go to HomeActivity
                    new AlertDialog.Builder(c)
                            .setTitle("Login Successful")
                            .setMessage(existingUsername + ", back again? Your owl missed you")
                            .setPositiveButton("Continue", (dialog, which) -> {
                                Intent intent = new Intent(c, HomeActivity.class);
                                intent.putExtra("username", existingUsername);
                                intent.putExtra("registeredEmail", registeredEmail);
                                intent.putExtra("registeredPassword", registeredPassword);
                                startActivity(intent);
                                finish();
                            })
                            .show();
                } else {
                    // If no username (1st time login), go to UsernameActivity
                    Intent intent = new Intent(c, UsernameActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("registeredEmail", registeredEmail);
                    intent.putExtra("registeredPassword", registeredPassword);
                    startActivity(intent);
                    finish();
                }

                // mali pass or email
            } else {
                new AlertDialog.Builder(c)
                        .setTitle("Login Failed")
                        .setMessage("Invalid email or password!")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

        //navigate to registerAct
        registerText.setOnClickListener(v -> {
            // Add confirmation dialog
            new AlertDialog.Builder(c)
                    .setMessage("You will be redirected to registration")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Intent intent = new Intent(c, RegisterActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}