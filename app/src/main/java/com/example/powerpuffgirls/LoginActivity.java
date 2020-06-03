package com.example.powerpuffgirls;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity { //SafeDelete Type Parameter

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // onAuthSuccess(currentUser);
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            finish();
        } else {
            SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
            music = MediaPlayer.create(this, R.raw.loginsound);
            music.setLooping(true);
            float vol = prefs.getFloat("loginVolume", 0.5f);
            music.setVolume(vol,vol);
            if (prefs.getBoolean("loginCheck", true)) {
                music.start();
            }
        }
        // updateUI(currentUser);
    }

    private void createAccount (final String name, String email, final String password) {
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
                                    onAuthSuccess(task.getResult().getUser(), user_name, name, id);
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
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void signIn (final String name, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    private static final String TAG = "Login";
                    String user_name = name;
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                            finish();
                            //pdateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    public void Login(View view) {
        String name = ((EditText)findViewById(R.id.name)).getText().toString();
        String NRIC = ((EditText)findViewById(R.id.NRIC)).getText().toString();
        NRIC += "@gmail.com";
        String password = ((EditText)findViewById(R.id.Password)).getText().toString();
        try {
            signIn(name, NRIC, password);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Invalud NRIC/Password",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void signUp(View view) {
        String name = ((EditText)findViewById(R.id.name)).getText().toString();
        String NRIC = ((EditText)findViewById(R.id.NRIC)).getText().toString();
        NRIC += "@gmail.com";
        String password = ((EditText)findViewById(R.id.Password)).getText().toString();
        try {
            createAccount(name, NRIC, password);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Invalid NRIC/Password",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (music != null) {
            music.release();
            music = null;
        }
    }

    private void getValuesFromDatabase(final OnGetDataListener listener) {
        listener.onStart();
        final String userid = mAuth.getUid();
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

    private void onAuthSuccess(FirebaseUser user, String name, ArrayList<String> nameslist, ArrayList<String> idslist) {
        // Write new user
        writeNewUser(user.getUid(), user.getEmail().substring(0, 9), name, nameslist, idslist);
    }

    private void writeNewUser(String userId, String nric, String name, ArrayList<String> nameslist, ArrayList<String> idslist) {
        //User user = new User(nric);

        nameslist.add(name);
        idslist.add(userId);

        String eContact1 = ((EditText)findViewById(R.id.eContact1)).getText().toString();
        String eContact2 = ((EditText)findViewById(R.id.eContact2)).getText().toString();

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

}
