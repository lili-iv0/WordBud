package com.example.madproject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordFrenzyActivity extends Navigation {

    // Screen states
    private static final String SCREEN_LEVEL_SELECT = "level_select";
    private static final String SCREEN_START = "start";
    private static final String SCREEN_QUIZ = "quiz";
    private static final String SCREEN_SCORE = "score";

    // Difficulty levels (all 10 questions now)
    private static final int HATCHLING_QUESTIONS = 10;
    private static final int SCHOLAR_QUESTIONS = 10;
    private static final int SAGE_OWL_QUESTIONS = 10;

    // UI Components
    private ImageView btnHatchling, btnScholar, btnSageOwl;
    private Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;
    private TextView tvQuestion, tvScore, tvQuestionNumber, tvScoreDisplay, tvCountdown, tvPercentage, tvInstructions, tvCountdownStart;
    private Button btnReturnHome, btnTryAgain;
    private ProgressBar progressBar;

    // Quiz state
    private List<Question> easyQuestions;
    private List<Question> mediumQuestions;
    private List<Question> hardQuestions;
    private List<Question> currentQuizQuestions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int totalQuestions = 0;
    private String correctAnswer;
    private boolean answerSelected = false;
    private String selectedDifficulty = "EASY";
    private CountDownTimer countDownTimer;
    private static final int COUNTDOWN_TIME = 30; // 30 seconds per question

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeQuestions();

        String screen = getIntent().getStringExtra("screen");
        if (screen == null) {
            showLevelSelectScreen();
        } else {
            switch (screen) {
                case SCREEN_START:
                    showStartScreen();
                    break;
                case SCREEN_QUIZ:
                    showQuizScreen();
                    break;
                case SCREEN_SCORE:
                    showScoreScreen();
                    break;
                default:
                    showLevelSelectScreen();
            }
        }
    }

    private void initializeQuestions() {
        easyQuestions = new ArrayList<>();
        mediumQuestions = new ArrayList<>();
        hardQuestions = new ArrayList<>();

        // HATCHLING (EASY) Questions
        easyQuestions.add(new Question("Move",
                "To go from one place to another",
                List.of("To remain in one place", "To push or pull something", "To forget")));
        easyQuestions.add(new Question("Mend",
                "To repair something broken",
                List.of("To break again", "To create something new", "To ignore damage")));
        easyQuestions.add(new Question("Notice",
                "To become aware of",
                List.of("To hide something", "To think quickly", "To memorize")));
        easyQuestions.add(new Question("Grasp",
                "To hold firmly",
                List.of("To release", "To imagine", "To let go")));
        easyQuestions.add(new Question("Choose",
                "To pick between options",
                List.of("To guess randomly", "To change one’s mind", "To avoid making a decision")));
        easyQuestions.add(new Question("Gather",
                "To collect or bring together",
                List.of("To scatter", "To separate", "To destroy")));
        easyQuestions.add(new Question("Seek",
                "To look for something",
                List.of("To give up searching", "To find accidentally", "To hide")));
        easyQuestions.add(new Question("Lend",
                "To let someone use something temporarily",
                List.of("To steal", "To borrow", "To sell")));
        easyQuestions.add(new Question("Claim",
                "To state ownership or truth",
                List.of("To guess randomly", "To hide", "To borrow")));
        easyQuestions.add(new Question("Resist",
                "To fight against or withstand",
                List.of("To give in easily", "To accept passively", "To avoid completely")));

        // SCHOLAR (MEDIUM) Questions
        mediumQuestions.add(new Question("Perceive",
                "To become aware through the senses or mind",
                List.of("To ignore", "To guess", "To forget")));
        mediumQuestions.add(new Question("Derive",
                "To obtain or come from a source",
                List.of("To create independently", "To destroy", "To separate")));
        mediumQuestions.add(new Question("Refrain",
                "To hold oneself back from doing something",
                List.of("To act impulsively", "To forget", "To encourage")));
        mediumQuestions.add(new Question("Concede",
                "To admit or accept after resisting",
                List.of("To deny strongly", "To argue", "To ignore")));
        mediumQuestions.add(new Question("Assert",
                "To state confidently or firmly",
                List.of("To question", "To suggest quietly", "To withdraw")));
        mediumQuestions.add(new Question("Devise",
                "To plan or invent something",
                List.of("To follow", "To destroy", "To copy")));
        mediumQuestions.add(new Question("Relinquish",
                "To give up or surrender",
                List.of("To fight for", "To claim", "To keep")));
        mediumQuestions.add(new Question("Endure",
                "To suffer patiently or last through hardship",
                List.of("To quit", "To escape", "To enjoy immediately")));
        mediumQuestions.add(new Question("Allude",
                "To refer indirectly to something",
                List.of("To explain fully", "To mention incorrectly", "To ignore")));
        mediumQuestions.add(new Question("Embark",
                "To begin a journey or new project",
                List.of("To finish", "To stop halfway", "To pause")));

        // SAGE OWL (HARD) Questions
        hardQuestions.add(new Question("Obfuscate",
                "To make unclear or confusing",
                List.of("To simplify clearly", "To reveal fully", "To explain quickly")));
        hardQuestions.add(new Question("Paragon",
                "A perfect example",
                List.of("A minor example", "A simple object", "A failure")));
        hardQuestions.add(new Question("Brevity",
                "Shortness or conciseness",
                List.of("Long-windedness", "Confusion", "Complexity")));
        hardQuestions.add(new Question("Abscond",
                "To flee secretly and hide",
                List.of("To appear openly", "To hide temporarily", "To wander casually")));
        hardQuestions.add(new Question("Ephemeral",
                "Lasting for a short time",
                List.of("Permanent", "Rare but permanent", "Fleeting yet unnoticed")));
        hardQuestions.add(new Question("Revere",
                "To respect deeply",
                List.of("To mock", "To ignore", "To avoid")));
        hardQuestions.add(new Question("Conundrum",
                "A confusing or difficult problem",
                List.of("Simple solution", "Easy task", "A funny joke")));
        hardQuestions.add(new Question("Lucid",
                "Clear and easy to understand",
                List.of("Confusing", "Hidden", "False")));
        hardQuestions.add(new Question("Morose",
                "Gloomy or sullen",
                List.of("Happy", "Busy", "Calm")));
        hardQuestions.add(new Question("Zealous",
                "Filled with great enthusiasm or passion",
                List.of("Lazy", "Quiet", "Weak")));

        // Shuffle each difficulty set
        Collections.shuffle(easyQuestions);
        Collections.shuffle(mediumQuestions);
        Collections.shuffle(hardQuestions);
    }


    private void showLevelSelectScreen() {
        setContentView(R.layout.activity_word_frenzy_level_select);

        // Assign nav buttons
        homeNavBtn = findViewById(R.id.homeNavBtn);
        addWordNavBtn = findViewById(R.id.addWordNavBtn);
        wordListNavBtn = findViewById(R.id.wordListNavBtn);
        profileNavBtn = findViewById(R.id.profileNavbtn);
        setupNavigation();

        btnHatchling = findViewById(R.id.btnHatchling);
        btnScholar = findViewById(R.id.btnScholar);
        btnSageOwl = findViewById(R.id.btnSageOwl);

        btnHatchling.setOnClickListener(v -> startQuiz(HATCHLING_QUESTIONS, "EASY"));
        btnScholar.setOnClickListener(v -> startQuiz(SCHOLAR_QUESTIONS, "MEDIUM"));
        btnSageOwl.setOnClickListener(v -> startQuiz(SAGE_OWL_QUESTIONS, "HARD"));
    }

    private void showStartScreen() {
        setContentView(R.layout.activity_word_frenzy_start);

        // Assign nav buttons
        homeNavBtn = findViewById(R.id.homeNavBtn);
        addWordNavBtn = findViewById(R.id.addWordNavBtn);
        wordListNavBtn = findViewById(R.id.wordListNavBtn);
        profileNavBtn = findViewById(R.id.profileNavbtn);
        setupNavigation();

        // Get total questions and difficulty from intent
        totalQuestions = getIntent().getIntExtra("totalQuestions", 10);
        selectedDifficulty = getIntent().getStringExtra("difficulty");
        if (selectedDifficulty == null) selectedDifficulty = "EASY";

        // Select questions based on difficulty
        List<Question> sourceQuestions;
        if (selectedDifficulty.equals("EASY")) {
            sourceQuestions = easyQuestions;
        } else if (selectedDifficulty.equals("MEDIUM")) {
            sourceQuestions = mediumQuestions;
        } else {
            sourceQuestions = hardQuestions;
        }

        int numQuestions = Math.min(10, sourceQuestions.size());
        currentQuizQuestions = new ArrayList<>(sourceQuestions.subList(0, numQuestions));
        Collections.shuffle(currentQuizQuestions);
        currentQuestionIndex = 0;
        score = 0;

        // Start countdown animation
        tvCountdownStart = findViewById(R.id.tvCountdownStart);
        startCountdownAnimation();
    }
    private void showQuizScreen() {
        setContentView(R.layout.activity_word_frenzy_quiz);

        // Assign nav buttons
        homeNavBtn = findViewById(R.id.homeNavBtn);
        addWordNavBtn = findViewById(R.id.addWordNavBtn);
        wordListNavBtn = findViewById(R.id.wordListNavBtn);
        profileNavBtn = findViewById(R.id.profileNavbtn);
        setupNavigation();

        tvQuestion = findViewById(R.id.tvQuestion);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvScoreDisplay = findViewById(R.id.tvScoreDisplay);
        tvCountdown = findViewById(R.id.tvCountdown);
        tvInstructions = findViewById(R.id.tvInstructions);
        progressBar = findViewById(R.id.progressBar);
        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        btnAnswer4 = findViewById(R.id.btnAnswer4);

        if (currentQuizQuestions == null || currentQuizQuestions.isEmpty()) {
            List<Question> sourceQuestions;
            if (selectedDifficulty.equals("EASY")) {
                sourceQuestions = easyQuestions;
            } else if (selectedDifficulty.equals("MEDIUM")) {
                sourceQuestions = mediumQuestions;
            } else {
                sourceQuestions = hardQuestions;
            }

            int numQuestions = Math.min(10, sourceQuestions.size());
            currentQuizQuestions = new ArrayList<>(sourceQuestions.subList(0, numQuestions));
            Collections.shuffle(currentQuizQuestions);
        }

        currentQuestionIndex = 0;
        score = 0;

        displayQuestion();
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= currentQuizQuestions.size()) {
            if (countDownTimer != null) countDownTimer.cancel();
            showScoreScreen();
            return;
        }

        answerSelected = false;
        Question currentQuestion = currentQuizQuestions.get(currentQuestionIndex);
        String cleanQuestionText = currentQuestion.getQuestion().replace("**", "");
        tvQuestion.setText(cleanQuestionText);
        correctAnswer = currentQuestion.getCorrectAnswer();

        if (tvQuestionNumber != null) tvQuestionNumber.setText("Question " + (currentQuestionIndex + 1) + "/" + totalQuestions);
        if (tvScoreDisplay != null) tvScoreDisplay.setText("Score: " + score);

        int progress = (int) (((currentQuestionIndex + 1) / (float) totalQuestions) * 100);
        progressBar.setProgress(progress);

        startCountdown();

        List<String> shuffledAnswers = currentQuestion.getAllAnswersShuffled();
        btnAnswer1.setText(shuffledAnswers.get(0));
        btnAnswer2.setText(shuffledAnswers.get(1));
        btnAnswer3.setText(shuffledAnswers.get(2));
        btnAnswer4.setText(shuffledAnswers.get(3));

        btnAnswer1.setBackgroundResource(R.drawable.button_white);
        btnAnswer2.setBackgroundResource(R.drawable.button_white);
        btnAnswer3.setBackgroundResource(R.drawable.button_white);
        btnAnswer4.setBackgroundResource(R.drawable.button_white);

        btnAnswer1.setBackgroundTintList(null);
        btnAnswer2.setBackgroundTintList(null);
        btnAnswer3.setBackgroundTintList(null);
        btnAnswer4.setBackgroundTintList(null);

        btnAnswer1.setEnabled(true);
        btnAnswer2.setEnabled(true);
        btnAnswer3.setEnabled(true);
        btnAnswer4.setEnabled(true);

        btnAnswer1.setOnClickListener(v -> checkAnswer(btnAnswer1, shuffledAnswers.get(0)));
        btnAnswer2.setOnClickListener(v -> checkAnswer(btnAnswer2, shuffledAnswers.get(1)));
        btnAnswer3.setOnClickListener(v -> checkAnswer(btnAnswer3, shuffledAnswers.get(2)));
        btnAnswer4.setOnClickListener(v -> checkAnswer(btnAnswer4, shuffledAnswers.get(3)));
    }

    private void startCountdown() {
        if (countDownTimer != null) countDownTimer.cancel();
        tvCountdown.setText(String.valueOf(COUNTDOWN_TIME));

        countDownTimer = new CountDownTimer(COUNTDOWN_TIME * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                tvCountdown.setText(String.valueOf(secondsLeft));
                if (secondsLeft <= 5) {
                    tvCountdown.setTextColor(ContextCompat.getColor(WordFrenzyActivity.this, android.R.color.holo_red_dark));
                } else {
                    tvCountdown.setTextColor(ContextCompat.getColor(WordFrenzyActivity.this, R.color.white));
                }
            }

            @Override
            public void onFinish() {
                tvCountdown.setText("0");
                tvCountdown.setTextColor(ContextCompat.getColor(WordFrenzyActivity.this, android.R.color.holo_red_dark));

                if (btnAnswer1.getText().toString().equals(correctAnswer)) btnAnswer1.setBackgroundResource(R.drawable.button_correct);
                else if (btnAnswer2.getText().toString().equals(correctAnswer)) btnAnswer2.setBackgroundResource(R.drawable.button_correct);
                else if (btnAnswer3.getText().toString().equals(correctAnswer)) btnAnswer3.setBackgroundResource(R.drawable.button_correct);
                else if (btnAnswer4.getText().toString().equals(correctAnswer)) btnAnswer4.setBackgroundResource(R.drawable.button_correct);

                btnAnswer1.setEnabled(false);
                btnAnswer2.setEnabled(false);
                btnAnswer3.setEnabled(false);
                btnAnswer4.setEnabled(false);

                new Handler().postDelayed(() -> {
                    currentQuestionIndex++;
                    displayQuestion();
                }, 1000);
            }
        }.start();
    }

    private void checkAnswer(Button selectedButton, String selectedAnswer) {
        if (answerSelected) return;
        answerSelected = true;
        if (countDownTimer != null) countDownTimer.cancel();

        btnAnswer1.setEnabled(false);
        btnAnswer2.setEnabled(false);
        btnAnswer3.setEnabled(false);
        btnAnswer4.setEnabled(false);

        btnAnswer1.setBackgroundTintList(null);
        btnAnswer2.setBackgroundTintList(null);
        btnAnswer3.setBackgroundTintList(null);
        btnAnswer4.setBackgroundTintList(null);

        if (btnAnswer1.getText().toString().equals(correctAnswer)) btnAnswer1.setBackgroundResource(R.drawable.button_correct);
        else if (btnAnswer2.getText().toString().equals(correctAnswer)) btnAnswer2.setBackgroundResource(R.drawable.button_correct);
        else if (btnAnswer3.getText().toString().equals(correctAnswer)) btnAnswer3.setBackgroundResource(R.drawable.button_correct);
        else if (btnAnswer4.getText().toString().equals(correctAnswer)) btnAnswer4.setBackgroundResource(R.drawable.button_correct);

        if (selectedAnswer.equals(correctAnswer)) {
            selectedButton.setBackgroundResource(R.drawable.button_correct);
            score++;
            if (tvScoreDisplay != null) tvScoreDisplay.setText("Score: " + score);
        } else {
            selectedButton.setBackgroundResource(R.drawable.button_wrong);
        }

        btnAnswer1.post(() -> btnAnswer1.invalidate());
        btnAnswer2.post(() -> btnAnswer2.invalidate());
        btnAnswer3.post(() -> btnAnswer3.invalidate());
        btnAnswer4.post(() -> btnAnswer4.invalidate());

        new Handler().postDelayed(() -> {
            currentQuestionIndex++;
            displayQuestion();
        }, 1500);
    }

    private void showScoreScreen() {
        setContentView(R.layout.activity_word_frenzy_score);

        homeNavBtn = findViewById(R.id.homeNavBtn);
        addWordNavBtn = findViewById(R.id.addWordNavBtn);
        wordListNavBtn = findViewById(R.id.wordListNavBtn);
        profileNavBtn = findViewById(R.id.profileNavbtn);
        setupNavigation();

        tvScore = findViewById(R.id.tvScore);
        tvPercentage = findViewById(R.id.tvPercentage);
        btnReturnHome = findViewById(R.id.btnReturnHome);
        btnTryAgain = findViewById(R.id.btnTryAgain);

        int percentage = 0;
        if (totalQuestions > 0) percentage = Math.round((score / (float) totalQuestions) * 100);
        percentage = Math.max(0, Math.min(100, percentage));
        animateScoreDisplay(score, totalQuestions, percentage);

        btnTryAgain.setOnClickListener(v -> {
            currentQuestionIndex = 0;
            score = 0;

            Intent intent = new Intent(c, WordFrenzyActivity.class);
            intent.putExtra("screen", SCREEN_START);
            intent.putExtra("username", username);
            intent.putExtra("totalQuestions", totalQuestions);
            intent.putExtra("difficulty", selectedDifficulty);
            startActivity(intent);
            finish();
        });

        btnReturnHome.setOnClickListener(v -> {
            Intent intent = new Intent(c, HomeActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        });
    }

    private void animateScoreDisplay(int finalScore, int finalTotal, int finalPercentage) {
        tvScore.setText(finalScore + " / " + finalTotal);
        tvPercentage.setText(finalPercentage + "%");
    }

    @Override
    protected void setupNavigation() {
        if (getIntent() != null) username = getIntent().getStringExtra("username");

        if (homeNavBtn != null) homeNavBtn.setOnClickListener(v -> showExitDialog(HomeActivity.class));
        if (addWordNavBtn != null) addWordNavBtn.setOnClickListener(v -> showExitDialog(AddWordActivity.class));
        if (wordListNavBtn != null) wordListNavBtn.setOnClickListener(v -> showExitDialog(WordListActivity.class));
        if (profileNavBtn != null) profileNavBtn.setOnClickListener(v -> showExitDialog(ProfileActivity.class));
    }

    private void showExitDialog(Class<?> targetActivity) {
        new AlertDialog.Builder(this)
                .setTitle("Exit Word Frenzy?")
                .setMessage("Your progress will not be saved. Are you sure you want to leave Word Frenzy?")
                .setPositiveButton("Exit", (dialog, which) -> {
                    if (countDownTimer != null) countDownTimer.cancel();
                    Intent intent = new Intent(c, targetActivity);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Stay", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    private void startCountdownAnimation() {
        final String[] countdown = {"3", "2", "1", "START!"};
        final int[] index = {0};

        Handler handler = new Handler();
        Runnable countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (index[0] < countdown.length) {
                    tvCountdownStart.setText(countdown[index[0]]);
                    index[0]++;
                    if (index[0] < countdown.length) handler.postDelayed(this, 1000);
                    else handler.postDelayed(() -> showQuizScreen(), 500);
                }
            }
        };
        handler.post(countdownRunnable);
    }

    private void startQuiz(int questionCount, String difficulty) {
        totalQuestions = questionCount;
        selectedDifficulty = difficulty;
        currentQuestionIndex = 0;
        score = 0;

        Intent intent = new Intent(c, WordFrenzyActivity.class);
        intent.putExtra("screen", SCREEN_START);
        intent.putExtra("username", username);
        intent.putExtra("totalQuestions", totalQuestions);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }
}


