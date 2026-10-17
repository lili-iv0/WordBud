package com.example.madproject;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.google.android.material.card.MaterialCardView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MindFlipActivity extends Navigation {

    Spinner spinnerSets;
    TextView tvCardCount, tvWord;
    Button btnPrev, btnFlip, btnNext;
    MaterialCardView cardWord;


    private ArrayList<String[]> currentList = new ArrayList<>();
    private int currentIndex = 0;
    private boolean showingWord = true;

    private AnimatorSet frontAnim, backAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mind_flip);

        // 🧭 Navigation setup
        homeNavBtn = findViewById(R.id.homeNavBtn);
        addWordNavBtn = findViewById(R.id.addWordNavBtn);
        wordListNavBtn = findViewById(R.id.wordListNavBtn);
        profileNavBtn = findViewById(R.id.profileNavbtn);
        setupNavigation();

        // 🧩 Initialize views
        spinnerSets = findViewById(R.id.spinnerSets);
        tvCardCount = findViewById(R.id.tvCardCount);
        tvWord = findViewById(R.id.tvWord);
        cardWord = findViewById(R.id.cardWord);
        btnPrev = findViewById(R.id.btnPrev);
        btnFlip = findViewById(R.id.btnFlip);
        btnNext = findViewById(R.id.btnNext);

        setupSpinner();
        setupButtons();
        setupFlipAnimation();
    }

    private void setupSpinner() {
        // 🔹 Get set names
        List<String> setNames = new ArrayList<>();
        if (CategoryPageActivity.userWordLists != null) {
            for (Map.Entry<String, ArrayList<String[]>> entry : CategoryPageActivity.userWordLists.entrySet()) {
                setNames.add(entry.getKey());
            }
        }

        if (setNames.isEmpty()) {
            setNames.add("No sets available");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, setNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSets.setAdapter(adapter);

        spinnerSets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSet = setNames.get(position);
                currentList = CategoryPageActivity.userWordLists.get(selectedSet);
                currentIndex = 0;
                updateCard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupButtons() {
        btnPrev.setOnClickListener(v -> {
            if (currentList == null || currentList.isEmpty()) return;
            currentIndex = (currentIndex - 1 + currentList.size()) % currentList.size();
            showingWord = true;
            updateCard();
        });

        btnNext.setOnClickListener(v -> {
            if (currentList == null || currentList.isEmpty()) return;
            currentIndex = (currentIndex + 1) % currentList.size();
            showingWord = true;
            updateCard();
        });

        btnFlip.setOnClickListener(v -> flipCard());
    }

    private void setupFlipAnimation() {
        frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.front_animator);
        backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.back_animator);
    }

    private void flipCard() {
        if (currentList == null || currentList.isEmpty()) return;

        cardWord.setCameraDistance(8000 * getResources().getDisplayMetrics().density);
        if (showingWord) {
            frontAnim.setTarget(tvWord);
            frontAnim.start();
            tvWord.setText(currentList.get(currentIndex)[1]); // definition
            backAnim.setTarget(tvWord);
            backAnim.start();
        } else {
            frontAnim.setTarget(tvWord);
            frontAnim.start();
            tvWord.setText(currentList.get(currentIndex)[0]); // word
            backAnim.setTarget(tvWord);
            backAnim.start();
        }
        showingWord = !showingWord;
    }

    private void updateCard() {
        if (currentList == null || currentList.isEmpty()) {
            tvWord.setText("No words yet.");
            tvCardCount.setText("0/0");
            return;
        }

        String[] current = currentList.get(currentIndex);
        tvWord.setText(current[0]); // Show word first
        tvCardCount.setText((currentIndex + 1) + "/" + currentList.size());
    }
}
