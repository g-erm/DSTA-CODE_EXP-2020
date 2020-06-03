package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DeclarationActivity extends AppCompatActivity {

    private Spinner countryChoices;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration);

        getUserDetails();

        countryChoices = findViewById(R.id.country);

        final String[] countries = getResources().getStringArray(R.array.country_choices);
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryChoices.setAdapter(adapter);

        Button submit = findViewById(R.id.submitTravel);
        final EditText name = findViewById(R.id.fullname);
        final EditText departure = findViewById(R.id.departure);
        final EditText returnDate = findViewById(R.id.returnDate);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departureString = departure.getText().toString();
                String returnString = returnDate.getText().toString();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy @ hh-mm-ss");
                String format = simpleDateFormat.format(new Date());

                mDatabase.child("users").child(mAuth.getUid()).child("travel").child(format).child("country").setValue(countryChoices.getSelectedItem().toString());
                mDatabase.child("users").child(mAuth.getUid()).child("travel").child(format).child("departure").setValue(departureString);
                mDatabase.child("users").child(mAuth.getUid()).child("travel").child(format).child("return").setValue(returnString);

                name.setText("");
                departure.setText("");
                returnDate.setText("");
                countryChoices.setAdapter(adapter);
            }
        });

        Button submit2 = findViewById(R.id.submitHealth);
        final EditText temperature = findViewById(R.id.tempInput);
        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = temperature.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String format = simpleDateFormat.format(new Date());

                mDatabase.child("users").child(mAuth.getUid()).child("health").child(format).child("temperature").setValue(temp);

                temperature.setText("");
            }
        });
    }

    protected void getUserDetails() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
