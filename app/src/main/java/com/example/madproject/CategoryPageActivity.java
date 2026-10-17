package com.example.madproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CategoryPageActivity extends Navigation {

    // Predefined words for categories
    private final String[][] actionWords = {
            {"Ascend", "To move or go up."},
            {"Conquer", "To successfully overcome or defeat."},
            {"Retrieve", "To get something back."},
            {"Devour", "To eat something eagerly or quickly."},
            {"Endure", "To suffer patiently or tolerate something difficult."}
    };

    private final String[][] adjectiveWords = {
            {"Oblivious", "Not aware of or not paying attention to something."},
            {"Vivid", "Producing strong, clear images in the mind."},
            {"Tedious", "Too long, slow, or dull."},
            {"Brisk", "Quick and energetic."},
            {"Fragile", "Easily broken or damaged."}
    };

    private final String[][] thingsWords = {
            {"Relic", "An object surviving from an earlier time."},
            {"Artifact", "An object made by humans, typically of cultural interest."},
            {"Contraption", "A strange or complicated device."},
            {"Apparatus", "A complex structure within a system or machine."},
            {"Trinket", "A small ornament or item of little value."}
    };

    private final String[][] technologyWords = {
            {"Algorithm", "A process or set of rules to solve a problem or perform a task."},
            {"Bandwidth", "The amount of data that can be transmitted in a fixed time."},
            {"Cache", "Stores data for quick access."},
            {"Encryption", "Converting information into code for security."},
            {"Interface", "A shared boundary where two systems interact."}
    };

    private final String[][] expressionsWords = {
            {"Break the ice", "Start a friendly conversation."},
            {"Hit the sack", "Go to sleep."},
            {"Piece of cake", "Something easy to do."},
            {"Spill the beans", "Reveal a secret."},
            {"Under the weather", "Feeling sick or unwell."}
    };

    // Shared user-created lists
    // LinkedHashMap preserves insertion order
    public static LinkedHashMap<String, ArrayList<String[]>> userWordLists = new LinkedHashMap<>();

    // Maintain order of user-created lists separately
    public static ArrayList<String> userCreatedLists = new ArrayList<>();

    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabvista_select_word);

        homeNavBtn = findViewById(R.id.homeNavBtn);
        addWordNavBtn = findViewById(R.id.addWordNavBtn);
        wordListNavBtn = findViewById(R.id.wordListNavBtn);
        profileNavBtn = findViewById(R.id.profileNavbtn);


        // Initialize userWordLists if null
        if (CategoryPageActivity.userWordLists == null) {
            CategoryPageActivity.userWordLists = new LinkedHashMap<>();
        }
        if (CategoryPageActivity.userCreatedLists == null) {
            CategoryPageActivity.userCreatedLists = new ArrayList<>();
        }

        // Ensure base lists exist and are initialized
        if (!CategoryPageActivity.userWordLists.containsKey("General")) {
            CategoryPageActivity.userWordLists.put("General", new ArrayList<>());
        }
        if (!CategoryPageActivity.userWordLists.containsKey("Favorites")) {
            CategoryPageActivity.userWordLists.put("Favorites", new ArrayList<>());
        }

        String categoryName = getIntent().getStringExtra("category");

        TextView[] wordViews = {
                findViewById(R.id.wordText1),
                findViewById(R.id.wordText2),
                findViewById(R.id.wordText3),
                findViewById(R.id.wordText4),
                findViewById(R.id.wordText5)
        };

        TextView[] defViews = {
                findViewById(R.id.defText1),
                findViewById(R.id.defText2),
                findViewById(R.id.defText3),
                findViewById(R.id.defText4),
                findViewById(R.id.defText5)
        };

        String[][] selectedSet;
        switch (categoryName) {
            case "Action": selectedSet = actionWords; break;
            case "Adjective": selectedSet = adjectiveWords; break;
            case "Things": selectedSet = thingsWords; break;
            case "Technology": selectedSet = technologyWords; break;
            case "Expressions": selectedSet = expressionsWords; break;
            default: selectedSet = new String[0][0];
        }

        for (int i = 0; i < selectedSet.length && i < 5; i++) {
            String w = selectedSet[i][0];
            String d = selectedSet[i][1];
            wordViews[i].setText(w);
            defViews[i].setText(d);

            wordViews[i].setOnClickListener(v -> showWordModal(w, d));
        }
    }

    private void showWordModal(String word, String definition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View modalView = LayoutInflater.from(this).inflate(R.layout.activity_vista_modal, null);

        Spinner spinner = modalView.findViewById(R.id.spinnerWordList);
        EditText txtWord = modalView.findViewById(R.id.txtWord);
        EditText txtDefinition = modalView.findViewById(R.id.txtDefinition);
        Button btnAddWord = modalView.findViewById(R.id.btnAddWord);
        ImageButton btnClose = modalView.findViewById(R.id.btnClose);

        txtWord.setText(word);
        txtDefinition.setText(definition);

        // Maintain proper order: General → Favorites → user-created lists
        ArrayList<String> listNames = new ArrayList<>();
        listNames.add("General");   // always first
        listNames.add("Favorites"); // always second
        if (userCreatedLists != null) {
            listNames.addAll(userCreatedLists); // newly created lists in order
        }

        spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, listNames);
        spinner.setAdapter(spinnerAdapter);

        builder.setView(modalView);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        btnAddWord.setOnClickListener(v -> {
            String selectedList = spinner.getSelectedItem().toString();

            // Ensure the list exists
            if (!userWordLists.containsKey(selectedList)) {
                userWordLists.put(selectedList, new ArrayList<>());

                // If it's a new user-created list, add to userCreatedLists
                if (!selectedList.equals("General") && !selectedList.equals("Favorites")) {
                    userCreatedLists.add(selectedList);
                }
            }

            ArrayList<String[]> list = userWordLists.get(selectedList);

            if (list != null) {
                list.add(new String[]{word, definition});

                Toast.makeText(this, "\"" + word + "\" added to " + selectedList, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, WordListActivity.class);
                intent.putExtra("SELECTED_LIST", selectedList);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}