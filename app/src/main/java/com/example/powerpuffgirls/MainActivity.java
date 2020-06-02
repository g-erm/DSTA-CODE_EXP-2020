package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
//        SharedPreferences.Editor preferencesEditor = pref.edit();
//        preferencesEditor.clear();
//        preferencesEditor.apply();

        Thread thread = new Thread() {
            @Override
            public void run () {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // If login details cached on device, run game
                    if (false) { //force to login page for now
                        Intent menuIntent = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(menuIntent);
                    } else {
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                    }
                    finish();
                }
            }
        };
        thread.start();

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        MediaPlayer music = MediaPlayer.create(this, R.raw.introsong);
        music.setVolume(.5f, .5f);
        if (prefs.getBoolean("splashCheck", true)) {
            music.start();
        }
    }
}
