package com.example.powerpuffgirls;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 100;

    private FirebaseAuth mAuth;
    public static MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();

        final SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);

        music = MediaPlayer.create(this, R.raw.wamengti);
        music.setLooping(true);
        float vol = prefs.getFloat("menuVolume", 0.5f);
        music.setVolume(vol,vol);
        if (prefs.getBoolean("menuCheck", true)) {
            music.start();
        }

        //Check whether this app has access to the location permission//

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        // For Ray

//        int permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
//        int permission3 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If the location permission has been granted, then start the TrackerService//

        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {

            //If the app doesn’t currently have access to the user’s location, then request access//

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }


//        if (permission2 == PackageManager.PERMISSION_DENIED) {
//            Log.d("Text", "");
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.RECORD_AUDIO},
//                    PERMISSIONS_REQUEST);
//        }
//        if (permission3 == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    PERMISSIONS_REQUEST);
//        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {

        //If the permission has been granted...//

        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //...then start the GPS tracking service//

            startTrackerService();
        } else {

            //If the user denies the permission request, then display a toast with some more information//

            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

//Start the TrackerService//

    private void startTrackerService() {
        startService(new Intent(this, TrackingService.class));

        //Notify the user that tracking has been enabled//

        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();

    }

    public void gotoTrace(View view) {
        startActivity(new Intent(MenuActivity.this, ContactTracingActivity.class));
    }

    public void gotoDeclaration(View view) {
        startActivity(new Intent(MenuActivity.this, DeclarationActivity.class));
    }

    public void gotoStayhome(View view) {
        startActivity(new Intent(MenuActivity.this, StayHomeActivity.class));
    }

    public void gotoSOS(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure? Pressing 'yes' will notify all your emergency contacts")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MenuActivity.this, SOSActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void gotoHelp(View view) {
        startActivity(new Intent(MenuActivity.this, HelpActivity.class));
    }

    public void gotoSettings(View view) {
        startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
    }


    public void gotoSignout(View view) {
        mAuth.signOut();
        stopService(new Intent(this, TrackingService.class));
        startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        finish();
    }

    public void gotoFriends(View view) {
        startActivity(new Intent(MenuActivity.this, FriendsActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (music != null) {
            music.release();
        }
    }
}
