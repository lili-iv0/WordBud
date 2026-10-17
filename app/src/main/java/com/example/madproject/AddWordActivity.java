package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class AddWordActivity extends Navigation {
    private Spinner listDropdown;
    private TextView createListText;
    private EditText wordInput, meaningInput, setInput;
    private LinearLayout addWordPopUp;
    private Button addWordBtn, addSetBtn;
    private ImageView closeAddSet;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        initializeViews();
        setupNavigation();
        initializeLists();
        initialize();
    }

    private void initializeViews() {
        listDropdown = findViewById(R.id.listDropdown);
        createListText = findViewById(R.id.createListText);
        wordInput = findViewById(R.id.wordInput);
        meaningInput = findViewById(R.id.meaningInput);
        addWordPopUp = findViewById(R.id.addWordPopUp);
        setInput = findViewById(R.id.setInput);
        addWordBtn = findViewById(R.id.addWordBtn);
        addSetBtn = findViewById(R.id.addSetBtn);
        closeAddSet = findViewById(R.id.closeAddSet);

        // Navigation
        homeNavBtn = findViewById(R.id.homeNavBtn);
        addWordNavBtn = findViewById(R.id.addWordNavBtn);
        wordListNavBtn = findViewById(R.id.wordListNavBtn);
        profileNavBtn = findViewById(R.id.profileNavbtn);
    }

    private void initializeLists() {
        // Ensure the static variables are initialized
        if (CategoryPageActivity.userWordLists == null) {
            CategoryPageActivity.userWordLists = new java.util.LinkedHashMap<>();
        }
        if (CategoryPageActivity.userCreatedLists == null) {
            CategoryPageActivity.userCreatedLists = new ArrayList<>();
        }

        // Ensure General and Favorites lists exist
        if (!CategoryPageActivity.userWordLists.containsKey("General")) {
            CategoryPageActivity.userWordLists.put("General", new ArrayList<>());
        }
        if (!CategoryPageActivity.userWordLists.containsKey("Favorites")) {
            CategoryPageActivity.userWordLists.put("Favorites", new ArrayList<>());
        }

        // Load existing lists
        lists.clear();
        lists.add("General");
        lists.add("Favorites");
        if (CategoryPageActivity.userCreatedLists != null) {
            lists.addAll(CategoryPageActivity.userCreatedLists);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lists);
        listDropdown.setAdapter(adapter);
    }

    private void initialize() {
        // Open popup to create new list
        createListText.setOnClickListener(v -> addWordPopUp.setVisibility(View.VISIBLE));

        // Close popup
        closeAddSet.setOnClickListener(v -> closePopup());

        // Add new list
        addSetBtn.setOnClickListener(v -> {
            String newList = setInput.getText().toString().trim();
            if (newList.isEmpty()) {
                showAlert("Empty Field", "Please enter a list name.");
                return;
            }

            if (lists.contains(newList)) {
                showAlert("Duplicate List", "A list with this name already exists.");
                return;
            }

            // Add to CategoryPage globals
            if (!CategoryPageActivity.userWordLists.containsKey(newList)) {
                CategoryPageActivity.userWordLists.put(newList, new ArrayList<>());
                CategoryPageActivity.userCreatedLists.add(newList); // preserve order
            }

            // Update local dropdown
            lists.clear();
            lists.add("General");
            lists.add("Favorites");
            lists.addAll(CategoryPageActivity.userCreatedLists);

            adapter.notifyDataSetChanged();
            listDropdown.setSelection(lists.size() - 1); // auto-select new list

            Toast.makeText(this, "List created: " + newList, Toast.LENGTH_SHORT).show();
            closePopup();
        });

        // Add word to selected list
        addWordBtn.setOnClickListener(v -> {
            String word = wordInput.getText().toString().trim();
            String meaning = meaningInput.getText().toString().trim();
            String selectedList = listDropdown.getSelectedItem().toString();

            if (word.isEmpty() || meaning.isEmpty()) {
                showAlert("Missing Information", "Please fill in all fields.");
                return;
            }

            // Ensure the selected list exists
            if (!CategoryPageActivity.userWordLists.containsKey(selectedList)) {
                CategoryPageActivity.userWordLists.put(selectedList, new ArrayList<>());

                // If it's a new user-created list, add to userCreatedLists
                if (!selectedList.equals("General") && !selectedList.equals("Favorites")) {
                    CategoryPageActivity.userCreatedLists.add(selectedList);
                }
            }

            // Add word to the selected list
            CategoryPageActivity.userWordLists.get(selectedList).add(new String[]{word, meaning});

            Toast.makeText(this, "Word added to " + selectedList, Toast.LENGTH_SHORT).show();

            // Clear inputs
            wordInput.setText("");
            meaningInput.setText("");
        });
    }

    private void closePopup() {
        addWordPopUp.setVisibility(View.GONE);
        setInput.setText("");
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}