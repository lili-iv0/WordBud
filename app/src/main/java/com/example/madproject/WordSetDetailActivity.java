package com.example.madproject;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class WordSetDetailActivity extends Navigation {

    LinearLayout wordListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list); // ✅ same layout you already use

        // assign nav buttons
        homeNavBtn = findViewById(R.id.homeNavBtn);
        addWordNavBtn = findViewById(R.id.addWordNavBtn);
        wordListNavBtn = findViewById(R.id.wordListNavBtn);
        profileNavBtn = findViewById(R.id.profileNavbtn);
        setupNavigation();

        wordListView = findViewById(R.id.wordListView);

        // 🟩 Get selected set name from intent
        String selectedSet = getIntent().getStringExtra("selectedSet");

        ArrayList<String[]> words = CategoryPageActivity.userWordLists.get(selectedSet);
        displayWordList(words);
    }

    private void displayWordList(ArrayList<String[]> words) {
        wordListView.removeAllViews();

        if (words == null || words.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No words yet in this list.");
            empty.setTextSize(18);
            wordListView.addView(empty);
            return;
        }

        for (String[] entry : words) {
            String word = entry[0];
            String def = entry[1];

            TextView textView = new TextView(this);
            textView.setText(word + " - " + def);
            textView.setTextSize(18);
            textView.setPadding(30, 40, 25, 40);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            textView.setBackgroundResource(R.drawable.word_container);

            // ❤️ heart toggle
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.heart_outline, 0);
            textView.setCompoundDrawablePadding(200);

            textView.setOnClickListener(v -> {
                boolean liked = "liked".equals(textView.getTag());
                textView.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, liked ? R.drawable.heart_outline : R.drawable.heart_filled, 0
                );
                textView.setTag(liked ? "unliked" : "liked");
            });

            wordListView.addView(textView);

            TextView spacer = new TextView(this);
            spacer.setHeight(60);
            wordListView.addView(spacer);
        }
    }
}
