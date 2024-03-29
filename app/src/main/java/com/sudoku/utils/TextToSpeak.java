package com.sudoku.utils;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

// Converst text (a String) to speech
// utilizes Google text to speech

public class TextToSpeak{

    private Context context;
    private Locale locale;
    private String string;
    private static TextToSpeech talk;

    // cons
    public TextToSpeak( Context context, Locale locale, String string){
        this.context = context;
        this.locale = locale;
        this.string = string;
        initialize();
    }

    // initialize text to speech function
    public void initialize(){
        talk = new TextToSpeech(context, new TextToSpeech.OnInitListener(){
            @Override
            public void onInit( int i) {
                if(i!= TextToSpeech.ERROR)
                    talk.setLanguage(locale);
                Speak(string);
            }
        });
    }

    // outputs string to speech
    public void Speak(String text){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            talk.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            talk.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

    }

}
