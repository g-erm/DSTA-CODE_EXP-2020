package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.5));

        final SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        if (prefs.contains("splashCheck")) {
            ((Switch)findViewById(R.id.splashSound)).setChecked(prefs.getBoolean("splashCheck", true));
        }
        if (prefs.contains("loginCheck")) {
            ((Switch)findViewById(R.id.loginSound)).setChecked(prefs.getBoolean("loginCheck", true));
        }
        if (prefs.contains("menuCheck")) {
            ((Switch)findViewById(R.id.menuSound)).setChecked(prefs.getBoolean("menuCheck", true));
        }
        if (prefs.contains("loginVolume")) {
            ((SeekBar)findViewById(R.id.seekLogin)).setProgress((int) (prefs.getFloat("loginVolume", 0.5f)*100));
        }
        if (prefs.contains("menuVolume")) {
            ((SeekBar)findViewById(R.id.seekMenu)).setProgress((int) (prefs.getFloat("menuVolume", 0.5f)*100));
        }

        ((Switch)findViewById(R.id.splashSound)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("splashCheck", isChecked);
                editor.apply();
            }
        });

        ((Switch)findViewById(R.id.loginSound)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("loginCheck", isChecked);
                editor.apply();
            }
        });

        ((Switch)findViewById(R.id.menuSound)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("menuCheck", isChecked);
                editor.apply();
                if (isChecked) {
                    MenuActivity.music.start();
                } else {
                    MenuActivity.music.pause();
                }
            }
        });

        ((SeekBar)findViewById(R.id.seekLogin)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float vol = ((float)progress)/100f;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putFloat("loginVolume", ((float)seekBar.getProgress())/100f);
                editor.apply();
            }
        });

        ((SeekBar)findViewById(R.id.seekMenu)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float vol = ((float)progress)/100f;
                MenuActivity.music.setVolume(vol,vol);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putFloat("menuVolume", ((float)seekBar.getProgress())/100f);
                editor.apply();
            }
        });
    }
}
