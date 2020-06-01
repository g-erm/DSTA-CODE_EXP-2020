package com.example.powerpuffgirls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class LoginActivity<EmailPasswordActivity> extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    boolean isLeavingApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        isLeavingApp = true;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // database key: firebase user.userId, val: nric
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            onAuthSuccess(currentUser);
//        }
        // updateUI(currentUser);
        if (currentUser != null) {
            isLeavingApp = false;
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            finish();
        }
    }

    private void createAccount (String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    private static final String TAG = "CreateUser";
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            onAuthSuccess(task.getResult().getUser());
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
                            onAuthSuccess(task.getResult().getUser());
                            isLeavingApp = false;
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
        String NRIC = ((EditText)findViewById(R.id.NRIC)).getText().toString();
        NRIC += "@gmail.com";
        String password = ((EditText)findViewById(R.id.Password)).getText().toString();
        try {
            createAccount(NRIC, password);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Invalid NRIC/Password",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isLeavingApp) {
            AudioPlay.music.pause();
        }
    }

    private void onAuthSuccess(FirebaseUser user) {
        // Write new user
        writeNewUser(user.getUid(), user.getEmail().substring(0, 9));
    }

    private void writeNewUser(String userId, String nric) {
        //User user = new User(nric);

        mDatabase.child("users").child(userId).setValue(nric);
    }

}
