package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class DeclarationActivity extends AppCompatActivity {

    private Spinner countryChoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration);

        countryChoices = findViewById(R.id.country);

        final String[] countries = getResources().getStringArray(R.array.country_choices);
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryChoices.setAdapter(adapter);

        Button submit = (Button) findViewById(R.id.submit);
        final EditText name = (EditText) findViewById(R.id.fullname);
        final EditText departure = (EditText) findViewById(R.id.departure);
        final EditText returnDate = (EditText) findViewById(R.id.returnDate);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                departure.setText("");
                returnDate.setText("");
                countryChoices.setAdapter(adapter);
            }
        });
    }
}
