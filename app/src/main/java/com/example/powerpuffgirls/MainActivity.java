package com.example.powerpuffgirls;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.FileInputStream;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
//        SharedPreferences.Editor preferencesEditor = pref.edit();
//        preferencesEditor.clear();
//        preferencesEditor.apply();

        //Check whether GPS tracking is enabled//
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

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
        music = MediaPlayer.create(this, R.raw.introsong);
        music.setVolume(.5f, .5f);
        if (prefs.getBoolean("splashCheck", true)) {
            music.start();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, GPS is required to use the app")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        music.release();
        music = null;
    }
}
