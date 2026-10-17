package com.example.madproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    TextView back;
    EditText firstName, lastName, email, password, confirmPassword;
    Button registerBtn;
    Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();
    }

    private void initialize() {
        back = findViewById(R.id.back);
        registerBtn = findViewById(R.id.registerBtn);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        //back button papunta login
        back.setOnClickListener(v -> new AlertDialog.Builder(c)
                .setTitle("Cancel")
                .setMessage("Cancel account creation?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    startActivity(new Intent(c, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("No", null)
                .show());

        registerBtn.setOnClickListener(v -> {
            String firstNameTxt = firstName.getText().toString().trim();
            String lastNameTxt = lastName.getText().toString().trim();
            String emailTxt = email.getText().toString().trim();
            String passwordTxt = password.getText().toString();
            String confirmPasswordTxt = confirmPassword.getText().toString();

            //alert if may empty
            if (firstNameTxt.isEmpty() || lastNameTxt.isEmpty()
                    || emailTxt.isEmpty() || passwordTxt.isEmpty() || confirmPasswordTxt.isEmpty()) {
                new AlertDialog.Builder(c)
                        .setTitle("Error")
                        .setMessage("Please fill in all fields!")
                        .setPositiveButton("OK", null)
                        .show();

                //alert if email is not valid
            } else if (!isValidEmail(emailTxt)) {
                new AlertDialog.Builder(c)
                        .setTitle("Error")
                        .setMessage("Please enter a valid email address!")
                        .setPositiveButton("OK", null)
                        .show();

                //alert magkaiba pass
            } else if (!passwordTxt.equals(confirmPasswordTxt)) {
                new AlertDialog.Builder(c)
                        .setTitle("Error")
                        .setMessage("Passwords do not match!")
                        .setPositiveButton("OK", null)
                        .show();

                //sumakses
            } else {
                Toast.makeText(c, "Account successfully created!", Toast.LENGTH_SHORT).show();

                //pass yung email & password sa loginAct para maka login yes the best
                Intent intent = new Intent(c, LoginActivity.class);
                intent.putExtra("registeredEmail", emailTxt);
                intent.putExtra("registeredPassword", passwordTxt);
                startActivity(intent);
                finish();
            }
        });
    }

    // Method to check if email is valid
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}