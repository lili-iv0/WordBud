package com.example.madproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    Button startBtn;
    Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initialize();
    }

    private void initialize() {
        startBtn = findViewById(R.id.startBtn);

        //navigate to loginAct
        startBtn.setOnClickListener(v -> {
            Intent intent = new Intent(c, LoginActivity.class);
            startActivity(intent);
        });
    }
}
