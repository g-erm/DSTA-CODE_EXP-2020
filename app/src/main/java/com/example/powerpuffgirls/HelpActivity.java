package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class HelpActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Debug Audio";
    private Button recordButton;
    private TextView recordText;

    private MediaRecorder recorder;
    private String fileName = null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        recordButton = findViewById(R.id.recordButton);
        recordText = findViewById(R.id.recordText);

        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName += "/recorded_audio.3gp";

        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();
                    recordText.setText("Recording Started...");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopRecording();
                    recordText.setText("Recording Stopped...");
                }
                return false;
            }
        });
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
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}
