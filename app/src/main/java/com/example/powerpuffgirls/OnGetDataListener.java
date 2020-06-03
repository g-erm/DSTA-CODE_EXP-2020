package com.example.powerpuffgirls;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public interface OnGetDataListener {
    void onSuccess(DataSnapshot dataSnapshot, ArrayList<String> name, ArrayList<String> id, ArrayList<String> friends);
    void onStart();
    void onFailure();
}
