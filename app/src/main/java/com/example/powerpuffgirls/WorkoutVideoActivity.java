package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.NumberPicker;
import android.widget.TextView;

public class WorkoutVideoActivity extends AppCompatActivity {

    NumberPicker choices;
    int difficultyNum;
    WebView webView;

    String[] easyStrings = {
            "10-Minute Yoga Stretching",
            "Stretch While Sitting Down",
            "Simple 10-Minute Daily Stretches"
    };

    String[] normStrings = {
            "Easy Taichi Workout",
            "Workout for Better Balance",
            "15-Minute Workout Routine"
    };

    String[] advStrings = {
            "15-Minute Workout Routine",
            "Zumba Session",
            "Line Dance Lesson"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_video);

        Intent caller = getIntent();
        final int difficultyChoice = caller.getIntExtra("Difficulty", 0);
        difficultyNum = difficultyChoice;
        final String difficultyString = caller.getStringExtra("Difficulty String");
        TextView textview = findViewById(R.id.chosenDiff);
        textview.setText(difficultyString + " Activities");
        webView = findViewById(R.id.webView);
        choices = findViewById(R.id.np_videos);
        String[] possibilitiesStrings;

        if (difficultyChoice == 0) {
            possibilitiesStrings = easyStrings;
        } else if (difficultyChoice == 1) {
            possibilitiesStrings = normStrings;
        } else {
            possibilitiesStrings = advStrings;
        }
        choices.setDisplayedValues(possibilitiesStrings);
        choices.setMinValue(0);
        choices.setMaxValue(possibilitiesStrings.length - 1);
    }

    public void goVideo(View v) {
        int videoNumber = choices.getValue();
        if (difficultyNum == 0) {
            if (videoNumber == 0) {
                if (MenuActivity.music != null)
                MenuActivity.music.pause();
                webView.loadUrl("https://www.youtube.com/watch?v=mK1q3b2jQqI");
            } else if (videoNumber == 1) {
                if (MenuActivity.music != null)
                MenuActivity.music.pause();
                webView.loadUrl("https://www.youtube.com/watch?v=YGRje8p5gbc");
            } else if (videoNumber == 2) {
                if (MenuActivity.music != null)
                MenuActivity.music.pause();
                webView.loadUrl("https://www.youtube.com/watch?v=eHXbj2Uq8mM");
            }
        } else if (difficultyNum == 1) {
            if (videoNumber == 0) {
                if (MenuActivity.music != null)
                MenuActivity.music.pause();
                webView.loadUrl("https://www.youtube.com/watch?v=cEOS2zoyQw4");
            } else if (videoNumber == 1) {
                if (MenuActivity.music != null)
                MenuActivity.music.pause();
                webView.loadUrl("https://www.youtube.com/watch?v=BNC4bi3Ucac");
            } else if (videoNumber == 2) {
                if (MenuActivity.music != null)
                MenuActivity.music.pause();
                webView.loadUrl("https://www.youtube.com/watch?v=Ev6yE55kYGw");
            }
        } else {
            if (videoNumber == 0) {
                if (MenuActivity.music != null)
                    MenuActivity.music.pause();
                webView.loadUrl("https://www.youtube.com/watch?v=2fpIva72q_k");
            } else if (videoNumber == 1) {
                if (MenuActivity.music != null)
                MenuActivity.music.pause();
                webView.loadUrl("https://www.youtube.com/watch?v=hcNhFTHh17s");
            } else if (videoNumber == 2) {
                if (MenuActivity.music != null)
                MenuActivity.music.pause();
                webView.loadUrl("https://www.youtube.com/watch?v=uZXQuqPnp8g");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        if (MenuActivity.music != null)
        if (!MenuActivity.music.isPlaying() && prefs.getBoolean("menuCheck", true) && MenuActivity.isPlaying) {
            MenuActivity.music.start();
        }
    }
}
