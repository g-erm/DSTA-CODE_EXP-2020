package com.example.powerpuffgirls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void signUp(View v) {
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String NRIC = ((EditText) findViewById(R.id.NRIC)).getText().toString();
        String eContact1 = ((EditText) findViewById(R.id.eContact1)).getText().toString();
        String eContact2 = ((EditText) findViewById(R.id.eContact2)).getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Empty Name",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (eContact1.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Empty Primary Contact",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (eContact2.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Empty Secondary Contact",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (NRIC.length() != 9) {
            Toast.makeText(getApplicationContext(), "Incorrect NRIC Length",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        NRIC += "@gmail.com";
        String password = ((EditText) findViewById(R.id.Password)).getText().toString();
        try {
            createAccount(name, NRIC, password, eContact1, eContact2);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Invalid NRIC/Password",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void onAuthSuccess(FirebaseUser user, String name, ArrayList<String> nameslist, ArrayList<String> idslist, String eContact1, String eContact2) {
        // Write new user
        writeNewUser(user.getUid(), user.getEmail().substring(0, 9), name, nameslist, idslist, eContact1, eContact2);
    }

    private void writeNewUser(String userId, String nric, String name, ArrayList<String> nameslist, ArrayList<String> idslist, String eContact1, String eContact2) {
        //User user = new User(nric);
        nameslist.add(name);
        idslist.add(userId);

        ArrayList<String> test = new ArrayList<>();
        test.add(userId);
        mDatabase.child("names").setValue(nameslist);
        mDatabase.child("ids").setValue(idslist);
        mDatabase.child("users").child(userId).child("friends").setValue(test);
        mDatabase.child("users").child(userId).child("profile").child("nric").setValue(nric);
        mDatabase.child("users").child(userId).child("profile").child("name").setValue(name);
        mDatabase.child("users").child(userId).child("profile").child("emergency 1").setValue(eContact1);
        mDatabase.child("users").child(userId).child("profile").child("emergency 2").setValue(eContact2);
    }

    private void createAccount (final String name, String email, final String password, final String eContact1, final String eContact2) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    private static final String TAG = "CreateUser";
                    String user_name = name;
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            getValuesFromDatabase(new OnGetDataListener() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot, ArrayList<String> name, ArrayList<String> id, ArrayList<String> friends) {
                                    onAuthSuccess(task.getResult().getUser(), user_name, name, id, eContact1, eContact2);
                                    finish();
                                    Toast.makeText(SignupActivity.this, "Proceed to login.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onStart() {
                                    Log.d("ONSTART", "Started");
                                }

                                @Override
                                public void onFailure() {
                                    Log.d("ONFAIL", "failed");
                                }
                            });
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Improve your password.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void getValuesFromDatabase(final OnGetDataListener listener) {
        listener.onStart();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> names = (ArrayList<String>) dataSnapshot.child("names").getValue();
                ArrayList<String> ids = (ArrayList<String>) dataSnapshot.child("ids").getValue();
                while (names == null && ids == null) {
                    try {
                        Log.d("sleep status", "still sleeping");
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ArrayList<String> empty = new ArrayList<>();
                listener.onSuccess(dataSnapshot, names, ids, empty);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public void back(View view) {
        finish();
    }
}