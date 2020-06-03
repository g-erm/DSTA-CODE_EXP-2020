package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);
    }


    public void gotoGame1(View view) {
        MenuActivity.music.pause();
        startActivity(new Intent(ChooseGameActivity.this, MainGameActivity.class));
    }

    public void gotoGame2(View view) {
        MenuActivity.music.pause();
        startActivity(new Intent(ChooseGameActivity.this, MainGame2Activity.class));
    }

    public void gotoGame3(View view) {
        MenuActivity.music.pause();
        startActivity(new Intent(ChooseGameActivity.this, SplashActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!MenuActivity.music.isPlaying()) {
            MenuActivity.music.start();
        }
    }
}
