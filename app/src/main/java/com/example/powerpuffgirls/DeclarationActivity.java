package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

        Button submit = (Button) findViewById(R.id.submit);
        final EditText name = (EditText) findViewById(R.id.fullname);
        final EditText departure = (EditText) findViewById(R.id.departure);
        final EditText returnDate = (EditText) findViewById(R.id.returnDate);
        final String country = countryChoices.getSelectedItem().toString();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                departure.setText("");
                returnDate.setText("");
                countryChoices.setAdapter(adapter);

                String departureString = departure.getText().toString();
                String returnString = returnDate.getText().toString();

                mDatabase.child("users").child(mAuth.getUid()).child("travel").child("country").setValue(country);
                mDatabase.child("users").child(mAuth.getUid()).child("travel").child("departure").child("date").setValue(departureString.substring(0,2));
                mDatabase.child("users").child(mAuth.getUid()).child("travel").child("departure").child("month").setValue(departureString.substring(3,5));
                mDatabase.child("users").child(mAuth.getUid()).child("travel").child("departure").child("year").setValue(departureString.substring(6));
                mDatabase.child("users").child(mAuth.getUid()).child("travel").child("return").child("date").setValue(returnString.substring(0,2));
                mDatabase.child("users").child(mAuth.getUid()).child("travel").child("return").child("month").setValue(returnString.substring(3,5));
                mDatabase.child("users").child(mAuth.getUid()).child("travel").child("return").child("year").setValue(returnString.substring(6));
            }
        });
    }

    protected void getUserDetails() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
