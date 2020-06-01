package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StayHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay_home);
    }

    public void gotoGame(View view) {
    }

    public void gotoWorkout(View view) {
        startActivity(new Intent(StayHomeActivity.this, WorkoutActivity.class));
    }
}
