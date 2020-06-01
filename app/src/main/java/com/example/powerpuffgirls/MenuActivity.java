package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    boolean isLeavingApp = true;
    private static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mediaPlayer = AudioPlay.music;
    }

    public void gotoTrace(View view) {
        startActivity(new Intent(MenuActivity.this, ContactTracingActivity.class));
    }

    public void gotoDeclaration(View view) {
        startActivity(new Intent(MenuActivity.this, DeclarationActivity.class));
    }

    public void gotoGame(View view) {
        startActivity(new Intent(MenuActivity.this, StayHomeActivity.class));
    }

    public void gotoWorkout(View view) {
        startActivity(new Intent(MenuActivity.this, WorkoutActivity.class));
    }

    public void gotoSOS(View view) {
        startActivity(new Intent(MenuActivity.this, SOSActivity.class));
    }

    public void gotoHelp(View view) {
        startActivity(new Intent(MenuActivity.this, HelpActivity.class));
    }

    public void gotoSettings(View view) {
        startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
    }


    public void gotoSignout(View view) {
        mAuth.signOut();
        finish();
        startActivity(new Intent(MenuActivity.this, LoginActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isLeavingApp) {
            AudioPlay.music.pause();
        }
    }
}
