package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class VocabVistaActivity extends Navigation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_vista);

        // 🟢 Get category name from main page
        String category = getIntent().getStringExtra("category");

        // assign nav buttons
        homeNavBtn = findViewById(R.id.homeNavBtn);
        addWordNavBtn = findViewById(R.id.addWordNavBtn);
        wordListNavBtn = findViewById(R.id.wordListNavBtn);
        profileNavBtn = findViewById(R.id.profileNavbtn);

        // 🟢 Find the image buttons
        ImageView btnActions = findViewById(R.id.btnActions);
        ImageView btnAdjective = findViewById(R.id.btnAdjective);
        ImageView btnThings = findViewById(R.id.btnThings);
        ImageView btnTechnology = findViewById(R.id.btnTechnology);
        ImageView btnExpressions = findViewById(R.id.btnExpressions);

        // 🟢 Optional: highlight or indicate which category was selected
        highlightCategory(category, btnActions, btnAdjective, btnThings, btnTechnology, btnExpressions);

        // 🟢 Set up clicks to go to CategoryPageActivity with the chosen category
        btnActions.setOnClickListener(v -> openCategory("Action"));
        btnAdjective.setOnClickListener(v -> openCategory("Adjective"));
        btnThings.setOnClickListener(v -> openCategory("Things"));
        btnTechnology.setOnClickListener(v -> openCategory("Technology"));
        btnExpressions.setOnClickListener(v -> openCategory("Expressions"));
    }

    private void openCategory(String categoryName) {
        Intent intent = new Intent(VocabVistaActivity.this, CategoryPageActivity.class);
        intent.putExtra("category", categoryName);
        startActivity(intent);
    }

    private void highlightCategory(String category,
                                   ImageView btnActions,
                                   ImageView btnAdjective,
                                   ImageView btnThings,
                                   ImageView btnTechnology,
                                   ImageView btnExpressions) {
        if (category == null) return;

        switch (category.toLowerCase()) {
            case "action":
                btnActions.setAlpha(1.0f);
                break;
            case "adjective":
                btnAdjective.setAlpha(1.0f);
                break;
            case "things":
                btnThings.setAlpha(1.0f);
                break;
            case "technology":
                btnTechnology.setAlpha(1.0f);
                break;
            case "expressions":
                btnExpressions.setAlpha(1.0f);
                break;
        }
    }
}
