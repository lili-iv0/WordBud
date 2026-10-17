package com.example.madproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {
    private String question;
    private String correctAnswer;
    private List<String> wrongAnswers;

    public Question(String question, String correctAnswer, List<String> wrongAnswers) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.wrongAnswers = wrongAnswers;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getWrongAnswers() {
        return wrongAnswers;
    }

    // Returns all answers shuffled
    public List<String> getAllAnswersShuffled() {
        List<String> allAnswers = new ArrayList<>();
        allAnswers.add(correctAnswer);
        allAnswers.addAll(wrongAnswers);
        Collections.shuffle(allAnswers);
        return allAnswers;
    }
}

