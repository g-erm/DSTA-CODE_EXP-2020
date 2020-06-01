package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainGameActivity extends AppCompatActivity {

    private boolean isMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        try {
                findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainGameActivity.this, GameActivity.class));
                    }
                });
        } catch (Exception e) {
            Log.d("25 @ Main Game Activity", e.getMessage());
        }


        TextView highScoreTxt = findViewById(R.id.highScoreTxt);

        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        if (prefs.contains("highscore")) {
            highScoreTxt.setText("HighScore: " + prefs.getInt("highscore", 0));
        }

        isMute = prefs.getBoolean("isMute", false);

        final ImageView volumeCtrl = findViewById(R.id.volumeCtrl);

        try {
            if (isMute) {
                volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
            } else {
                volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_up_24);
            }
        } catch (Exception e) {
            Log.d("49 @ Main Game Activity", e.getMessage());
        }

        try {
            volumeCtrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isMute = !isMute;
                    if (isMute) {
                        volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
                    } else {
                        volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_up_24);
                    }
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isMute", isMute);
                    editor.apply();

                }
            });
        } catch (Exception e) {
            Log.d("59 @ Main Game Activity", e.getMessage());
        }

    }
}