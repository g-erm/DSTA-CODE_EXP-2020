package com.example.powerpuffgirls;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView t = findViewById(R.id.text);
        t.setText("\n" + "\n" + "RULES FOR TIC-TAC-TOE\n" +
                "\n" +
                "1. The game is played on a grid that's 3 squares by 3 squares.\n" +
                "\n" +
                "2. You are X, your friend (or the computer in this case) is O. Players take turns putting their marks in empty squares.\n" +
                "\n" +
                "3. The first player to get 3 of her marks in a row (up, down, across, or diagonally) is the winner.\n" +
                "\n" +
                "4. When all 9 squares are full, the game is over. If no player has 3 marks in a row, the game ends in a tie.\n" +
                "\n");
    }

    public void BackToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), TTTMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
