package com.example.powerpuffgirls;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public interface OnGetGameListener {

    void onSuccess(DataSnapshot dataSnapshot, ArrayList<String> names);
    void onStart();
    void onFailure();

}
