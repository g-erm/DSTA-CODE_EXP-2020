package com.example.powerpuffgirls;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
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

public class LoginActivity extends AppCompatActivity { //SafeDelete Type Parameter

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private MediaPlayer music;
    private ArrayList<String> allId = new ArrayList<>();
    public HashMap<String, String> namesAndId = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getValuesFromDatabase();
        // database key: firebase user.userId, val: nric
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
            music = MediaPlayer.create(this, R.raw.loginsound);
            music.setLooping(true);
            music.setVolume(0.5f, 0.5f);
            music.start();
        }
        // updateUI(currentUser);
    }

    private void createAccount (final String name, String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    private static final String TAG = "CreateUser";
                    String user_name = name;
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            onAuthSuccess(task.getResult().getUser(), user_name);
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
                            onAuthSuccess(task.getResult().getUser(), user_name);
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
        }
    }

    private void getValuesFromDatabase() {
        mDatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getExistingMap(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getExistingMap(DataSnapshot dataSnapshot) {
        this.namesAndId = (HashMap<String, String>) dataSnapshot.child("names").getValue();
    }

    private void onAuthSuccess(FirebaseUser user, String name) {
        // Write new user
        writeNewUser(user.getUid(), user.getEmail().substring(0, 9), name);
    }

    private void writeNewUser(String userId, String nric, String name) {
        //User user = new User(nric);

        if (this.namesAndId == null) { this.namesAndId = new HashMap<>(); }
        this.namesAndId.put(name, userId);

        mDatabase.child("names").setValue(this.namesAndId);
        mDatabase.child("users").child(userId).child("profile").child("nric").setValue(nric);
        mDatabase.child("users").child(userId).child("profile").child("name").setValue(name);
    }

}
