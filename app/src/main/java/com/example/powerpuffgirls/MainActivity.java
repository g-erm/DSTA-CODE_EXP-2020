package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        MediaPlayer music = MediaPlayer.create(this, R.raw.introsong);
        music.setVolume(.8f, .8f);
        music.start();
    }
}
