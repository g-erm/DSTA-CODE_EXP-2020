package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.Toast;

public class MusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_music);

        final SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);

        if (MenuActivity.music == null) {
            MenuActivity.music = MediaPlayer.create(this, R.raw.wamengti);
            float vol = prefs.getFloat("menuVolume", 0.5f);
            MenuActivity.music.setVolume(vol, vol);
        }

        Button start = findViewById(R.id.PLAY);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getBoolean("menuCheck", true)) {
                    MenuActivity.isPlaying = true;
                    MenuActivity.music.start();
                }
                MenuActivity.music.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        MenuActivity.isPlaying = false;
                        Toast.makeText(MusicActivity.this, "The Song is Over", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Button stop = findViewById(R.id.STOP);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuActivity.isPlaying = false;
                MenuActivity.music.pause();
            }
        });
    }
}
