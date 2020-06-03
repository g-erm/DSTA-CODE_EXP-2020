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



    public void signIn (String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    private static final String TAG = "Login";
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
        String NRIC = ((EditText)findViewById(R.id.NRIC)).getText().toString();
        NRIC += "@gmail.com";
        String password = ((EditText)findViewById(R.id.Password)).getText().toString();
        try {
            signIn(NRIC, password);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Invalud NRIC/Password",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void signUp(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (music != null) {
            music.release();
            music = null;
        }
    }


}
