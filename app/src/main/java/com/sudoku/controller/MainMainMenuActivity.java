package com.sudoku.controller;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sudoku.R;
import com.sudoku.utils.CSVFile;
import com.sudoku.utils.MODE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainMainMenuActivity extends AppCompatActivity {
    private static final String TAG = "MainMainMenuActivity";
    private Button startBtn;
    Animation frombottom, fromtop;
    ImageView star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set the layout and the saved Instance State.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_main_menu);


        if (savedInstanceState != null) {

        }
        startBtn = (Button) findViewById(R.id.startBtn);
        star = (ImageView) findViewById(R.id.logo);
        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        startBtn.setAnimation(frombottom);
        star.setAnimation(fromtop);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MainActivity:
                Intent intent = new Intent(MainMainMenuActivity.this, MainMenuActivity.class);
                //to put info into the intent
                startActivity(intent);
                Log.d(TAG, "Main Menu Play btn pressed");

            }
        });


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
        Log.i(TAG, "onSaveInstanceState");
        //savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        //savedInstanceState.putIntArray(KEY_ANSWERS, userAnswers);
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


}