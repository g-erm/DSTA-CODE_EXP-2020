package com.example.powerpuffgirls;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.Objects;

public class HelpActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Debug Audio";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
//    private Button recordButton;
//    private TextView recordText;

    private MediaRecorder recorder;
    private static String fileName = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

//        recordButton = findViewById(R.id.recordButton);
//        recordText = findViewById(R.id.recordText);

        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audio.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        ((ToggleButton)findViewById(R.id.recordButton)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (true) {
                    startRecording();
                } else {
                    stopRecording();
                }
            }
        });

//        recordButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    startRecording();
//                    recordText.setText("Recording Started...");
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    stopRecording();
//                    recordText.setText("Recording Stopped...");
//                }
//                return false;
//            }
//        });
    }

    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();

    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Toast.makeText(this, "IO Exception Prepare", Toast.LENGTH_SHORT).show();
        }
        //catch (Exception e) {
        //    Toast.makeText(this, "Exception Prepare", Toast.LENGTH_SHORT).show();
        //}

        //try {
            recorder.start();
        //} catch (Exception e) {
        //    Toast.makeText(this, "Exception Start", Toast.LENGTH_SHORT).show();
        //}
    }

    private void stopRecording() {
        //try {
            recorder.stop();
            recorder.release();
            recorder = null;
        //} catch (Exception e) {
        //    Toast.makeText(this, "Recording Exception", Toast.LENGTH_SHORT).show();
        //}
    }
}
