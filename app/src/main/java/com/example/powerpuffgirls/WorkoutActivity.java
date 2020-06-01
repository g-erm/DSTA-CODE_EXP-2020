package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

public class WorkoutActivity extends AppCompatActivity {

    NumberPicker dpossibilities;
    String[] dpossibilitiesStrings = {
            "Light Stretching",
            "Normal Exercise",
            "Advanced"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        dpossibilities = (NumberPicker) findViewById(R.id.np_difficulty);
        dpossibilities.setDisplayedValues(dpossibilitiesStrings);
        dpossibilities.setMinValue(0);
        dpossibilities.setMaxValue(dpossibilitiesStrings.length - 1);
    }

    public void showVideos(View v) {
        int choice = dpossibilities.getValue();
        Intent toNext = new Intent();
        toNext.setClass(this, WorkoutVideoActivity.class);
        toNext.putExtra("Difficulty String", dpossibilitiesStrings[choice]);
        toNext.putExtra("Difficulty", choice);
        startActivity(toNext);
    }
}
