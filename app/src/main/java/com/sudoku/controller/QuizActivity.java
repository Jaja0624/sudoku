package com.sudoku.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sudoku.R;
import com.sudoku.model.Question;
import com.sudoku.model.Word;
import com.sudoku.utils.CSVFile;
import com.sudoku.utils.MODE;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";

    // english version of default word list for French mode
    private String[] eFrenchList = {"cat", "dog", "boy", "girl", "rat", "pig", "bye", "bad", "yes", "hello", "table", "red"};
    // french version of default word list for French mode
    private String[] oFrenchList = {"chat", "chien", "garcon", "fille", "rat", "porc", "au revoir", "mal", "oui", "bonjour", "tableau", "rouge"};

    // english version of default word list for Spanish mode
    private String[] eSpanishList = {"cat", "dog", "boy", "girl", "rat", "pig", "I", "bad", "yes", "hello", "table", "red"};
    // spanish version of default word list for Spanish mode
    private String[] oSpanishList = {"gata", "perra", "niño", "niña", "rata", "cerda", "yo", "mala", "sí", "hola", "mesa", "roja"};

    // timer
    private Chronometer timer;

    // word list to be quizzed
    private Question[] mQuestionBank;

    private int difficulty;

    // CSV MODE DOES NOT WORK RIGHT NOW
    private MODE gameMode;

    // GUI elements
    private TextView speechDisplayText; // text view to display user input
    private ImageButton speechBtn; // image btn for input

    private Question currentQuestion;
    private int answered; // number of correct answers
    private boolean gameOver;

    // # of words in quiz
    private int fin; // done <- answered - fin == 0

    // for speech to text functionality
    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;

    // for manual mode
    private String[] manEngList;
    private String[] manNonEngList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz);
        // initialize layout
        timer = findViewById(R.id.timer);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("ChronoTime")) {
                Long pastTimer = savedInstanceState.getLong("ChronoTime");
                Log.d(TAG, "past timer = " + pastTimer);
                timer.setBase(pastTimer);
                timer.start();
            }
        }
        checkPermission();
        speechDisplayText = (TextView)findViewById(R.id.speechDisplayText);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null) {
                    String matchOne = matches.get(0);
                    Log.d(TAG, "input = " + matchOne);
                    speechDisplayText.setText(matchOne);
                    checkAns(matchOne);
                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
            }
        });


        // the button for user to touch to speak
        speechBtn = findViewById(R.id.speechBtn);
        speechBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!gameOver) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_UP:
                            mSpeechRecognizer.stopListening();
                            speechDisplayText.setHint("");
                            Log.d(TAG, "up");
                            break;

                        case MotionEvent.ACTION_DOWN:
                            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                            Log.d(TAG, "down");
                            speechDisplayText.setText("");
                            speechDisplayText.setHint("Listening...");
                            break;
                    }
                } else {
                    newGame();
                }

                return false;
            }
        });

        Bundle extras = getIntent().getExtras();
        gameMode = (MODE)extras.getSerializable("selectedMode");
        if (gameMode == MODE.MANUAL) {
            manEngList = extras.getStringArray("engList");
            manNonEngList = extras.getStringArray("nonEngList");
        }
        difficulty = extras.getInt("difficulty");
        // layout  -------------------------
        fin = 0;
        if (difficulty == 1) {
            fin = 5;
        } else if (difficulty == 2) {
            fin = 8;
        } else if (difficulty == 3) {
            fin = 11;
        }
        // initialize question bank
        newGame();
    }

    // determines if input is correct answer
    // updates the GUI accordingly
    private void checkAns(String input) {
        // first display answer
        speechDisplayText.setText(input);
        if (match(currentQuestion, input)) {
            // answer is correct
            Log.d(TAG, "Highlight Green");
            speechDisplayText.setTextColor(Color.GREEN); // Green
            answered++;
            if (answered == fin) {
                // GAME OVER
                timer.stop();
                speechDisplayText.setText("QUIZ COMPLETE!");
                TextView question_text = findViewById(R.id.question_text);
                question_text.setText("QUIZ COMPLETE!");
                gameOver = true;
                Log.d(TAG, "Quiz done");
                return;
            } else {
                // NEXT QUESTION
                currentQuestion = mQuestionBank[answered];
                displayQuestion(mQuestionBank[answered]);
            }
            return;
        }
        // answer is false
        Log.d(TAG, "highlight red");
        speechDisplayText.setTextColor(Color.RED); // Not Green
    }

    // initializes a new game for quiz
    private void newGame() {
        resetTimer();
        initializeQuestionBank(gameMode);
        answered = 0;
        gameOver = false;
        Random rand = new Random();
        currentQuestion = mQuestionBank[rand.nextInt(fin+1)];
        displayQuestion(currentQuestion);
        speechDisplayText.setText("Hold the button below to answer");
    }

    // displays the current question string
    private void displayQuestion(Question q) {
        Log.d(TAG, "Setting question: " + q.getQuestion());
        TextView question_text = findViewById(R.id.question_text);
        question_text.setText(q.getQuestion());
    }

    // returns true if string matches the question string
    private boolean match(Question question, String answer) {
        return (question.getAnswer() == answer.toLowerCase()) || question.getAnswer().equals(answer.toLowerCase());
    }

    // determine which word list to use and initialize the quiz question bank
    private void initializeQuestionBank(MODE mode) {
        String[] tmpEngList, tmpNonEngList;
        if (mode == MODE.FRENCH) {
            tmpEngList = eFrenchList;
            tmpNonEngList = oFrenchList;
        } else if (mode == MODE.SPANISH) {
            tmpEngList = eSpanishList;
            tmpNonEngList = oSpanishList;
        } else if (mode == MODE.MANUAL) {
            tmpEngList = manEngList;
            tmpNonEngList = manNonEngList;
        } else {
            throw new IllegalArgumentException("Wrong mode. French/Spanish only");

        }

        mQuestionBank = new Question[fin];
        for (int i = 0; i < fin; i++) {
            mQuestionBank[i] = new Question(tmpNonEngList[i], tmpEngList[i]);
        }
        shuffleQuestionBank();
    }

    // Shuffles question bank so each game is not the same
    private void shuffleQuestionBank() {
        Random rand = new Random();
        for (int i = mQuestionBank.length - 1; i > 0; i--) {
            int index = rand.nextInt(i+1);
            Question tmp = mQuestionBank[index];
            mQuestionBank[index] = mQuestionBank[i];
            mQuestionBank[i] = tmp;
        }
    }

    // resets chrono timer
    private void resetTimer() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong("ChronoTime", timer.getBase());
        Log.i(TAG, "onSaveInstanceState");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    // check permissions..
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }
}


