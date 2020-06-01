package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Thread thread = new Thread() {
//            @Override
//            public void run () {
//                try {
//                    sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    Intent menuIntent = new Intent(LoginActivity.this, MenuActivity.class);
//                    startActivity(menuIntent);
//
//                }
//            }
//        };
//        thread.start();
    }


    public void gotoMenu(View view) {
        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
    }
}
