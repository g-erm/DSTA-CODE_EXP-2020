package com.example.powerpuffgirls;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TTTMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
    }

    public void startGame_SinglePlayer(View view){
        Intent intent = new Intent(getApplicationContext(), TTTGameActivity.class);
        startActivity(intent);
    }

    public void EndGame(View view) {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    public void ShowAboutNote(View view) {
        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(intent);
    }

    public void StartGameOnline(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginOnlineActivity.class);
        startActivity(intent);
    }
}
