package com.example.madproject;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class WordListActivity extends Navigation {

    LinearLayout wordListView;
    Spinner listDropdown;
    ArrayList<String> wordSets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        // Initialize navigation
        homeNavBtn = findViewById(R.id.homeNavBtn);
        addWordNavBtn = findViewById(R.id.addWordNavBtn);
        wordListNavBtn = findViewById(R.id.wordListNavBtn);
        profileNavBtn = findViewById(R.id.profileNavbtn);
        setupNavigation();

        wordListView = findViewById(R.id.wordListView);
        listDropdown = findViewById(R.id.listDropdown);

        // Ensure static variables are initialized
        if (CategoryPageActivity.userWordLists == null) {
            CategoryPageActivity.userWordLists = new java.util.LinkedHashMap<>();
        }
        if (CategoryPageActivity.userCreatedLists == null) {
            CategoryPageActivity.userCreatedLists = new ArrayList<>();
        }

        // Ensure Favorites list exists
        if (!CategoryPageActivity.userWordLists.containsKey("Favorites")) {
            CategoryPageActivity.userWordLists.put("Favorites", new ArrayList<>());
        }

        // Initialize Spinner with all lists
        refreshSpinner();

        // Check if we have a selected list from AddWordActivity or CategoryPageActivity
        String selectedListFromIntent = getIntent().getStringExtra("SELECTED_LIST");

        // Show words for selected list when Spinner changes
        listDropdown.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedList = wordSets.get(position);
                displayWordList(selectedList);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Set selection based on intent or default to General
        if (selectedListFromIntent != null && wordSets.contains(selectedListFromIntent)) {
            int position = wordSets.indexOf(selectedListFromIntent);
            listDropdown.setSelection(position);
            displayWordList(selectedListFromIntent);
        } else {
            displayWordList("General");
        }
    }

    /** Refresh spinner and ensure all lists exist */
    public void refreshSpinner() {
        wordSets = new ArrayList<>();

        // Always include General and Favorites
        wordSets.add("General");
        wordSets.add("Favorites");

        // Add user created lists
        if (CategoryPageActivity.userCreatedLists != null) {
            wordSets.addAll(CategoryPageActivity.userCreatedLists);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                wordSets
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listDropdown.setAdapter(adapter);
    }

    /** Display all words in a given list */
    public void displayWordList(String listName) {
        wordListView.removeAllViews();

        ArrayList<String[]> words = CategoryPageActivity.userWordLists.get(listName);

        if (words == null || words.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No words in " + listName + " list");
            emptyText.setTextSize(16);
            emptyText.setPadding(30, 40, 25, 40);
            emptyText.setTextColor(getResources().getColor(android.R.color.darker_gray));
            emptyText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            wordListView.addView(emptyText);
            return;
        }

        for (int i = 0; i < words.size(); i++) {
            String[] wordEntry = words.get(i);
            if (wordEntry.length < 2) continue; // Skip invalid entries

            String word = wordEntry[0];
            String meaning = wordEntry[1];
            final int wordIndex = i; // Store index for deletion

            // Create a horizontal layout to hold text and icons
            LinearLayout wordItemLayout = new LinearLayout(this);
            wordItemLayout.setOrientation(LinearLayout.HORIZONTAL);
            wordItemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            wordItemLayout.setPadding(30, 40, 25, 40);
            wordItemLayout.setBackgroundResource(R.drawable.word_container);
            wordItemLayout.setGravity(Gravity.CENTER_VERTICAL);

            // Create TextView for word and meaning
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1 // Weight to take available space
            ));
            textView.setText(word + " - " + meaning);
            textView.setTextSize(18);
            textView.setTextColor(getResources().getColor(android.R.color.black));

            // Check if word is already in Favorites to set initial heart state
            boolean isInFavorites = isWordInFavorites(word, meaning);

            // Create heart icon
            ImageView heartIcon = new ImageView(this);
            LinearLayout.LayoutParams heartParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            heartParams.setMargins(0, 0, 30, 0); // Add space between heart and trash
            heartIcon.setLayoutParams(heartParams);
            heartIcon.setImageResource(isInFavorites ? R.drawable.heart_filled : R.drawable.heart_outline);
            heartIcon.setTag(isInFavorites ? "liked" : "unliked");

            // Create trash icon
            ImageView trashIcon = new ImageView(this);
            trashIcon.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            trashIcon.setImageResource(R.drawable.trash); // Make sure you have this drawable

            // Heart icon click listener
            heartIcon.setOnClickListener(v -> {
                boolean currentlyLiked = "liked".equals(heartIcon.getTag());

                if (!currentlyLiked) {
                    // Add to Favorites
                    addToFavorites(word, meaning);
                    heartIcon.setImageResource(R.drawable.heart_filled);
                    heartIcon.setTag("liked");
                    Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                } else {
                    // Remove from Favorites
                    removeFromFavorites(word, meaning);
                    heartIcon.setImageResource(R.drawable.heart_outline);
                    heartIcon.setTag("unliked");
                    Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                }
            });

            // Trash icon click listener - pass the current word and meaning
            trashIcon.setOnClickListener(v -> {
                deleteWordFromList(listName, wordIndex, word, meaning);
            });

            // Add views to the horizontal layout
            wordItemLayout.addView(textView);
            wordItemLayout.addView(heartIcon);
            wordItemLayout.addView(trashIcon);

            // Add the horizontal layout to the main list
            wordListView.addView(wordItemLayout);

            // Spacer between words
            View spacer = new View(this);
            spacer.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    20
            ));
            spacer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            wordListView.addView(spacer);
        }
    }

    /** Delete word from the current list */
    private void deleteWordFromList(String listName, int wordIndex, String word, String meaning) {
        ArrayList<String[]> words = CategoryPageActivity.userWordLists.get(listName);

        if (words != null && wordIndex < words.size()) {
            // Store the word being deleted for confirmation
            String wordToDelete = words.get(wordIndex)[0];

            // Remove from current list
            words.remove(wordIndex);

            // Also remove from Favorites if it exists there
            removeFromFavorites(word, meaning);

            Toast.makeText(this, "\"" + wordToDelete + "\" deleted from " + listName, Toast.LENGTH_SHORT).show();

            // Refresh the display
            displayWordList(listName);
        }
    }

    /** Check if a word is already in Favorites list */
    private boolean isWordInFavorites(String word, String meaning) {
        ArrayList<String[]> favorites = CategoryPageActivity.userWordLists.get("Favorites");
        if (favorites == null) return false;

        for (String[] favWord : favorites) {
            if (favWord[0].equals(word) && favWord[1].equals(meaning)) {
                return true;
            }
        }
        return false;
    }

    /** Add word to Favorites list */
    private void addToFavorites(String word, String meaning) {
        ArrayList<String[]> favorites = CategoryPageActivity.userWordLists.get("Favorites");
        if (favorites == null) {
            favorites = new ArrayList<>();
            CategoryPageActivity.userWordLists.put("Favorites", favorites);
        }

        // Check if not already in favorites to avoid duplicates
        if (!isWordInFavorites(word, meaning)) {
            favorites.add(new String[]{word, meaning});
        }
    }

    /** Remove word from Favorites list */
    private void removeFromFavorites(String word, String meaning) {
        ArrayList<String[]> favorites = CategoryPageActivity.userWordLists.get("Favorites");
        if (favorites == null) return;

        for (int i = 0; i < favorites.size(); i++) {
            String[] favWord = favorites.get(i);
            if (favWord[0].equals(word) && favWord[1].equals(meaning)) {
                favorites.remove(i);
                break;
            }
        }
    }

    /** Call this method when returning to this activity */
    @Override
    protected void onResume() {
        super.onResume();
        refreshSpinner();
        String selectedList = (String) listDropdown.getSelectedItem();
        if (selectedList != null) {
            displayWordList(selectedList);
        }
    }
}