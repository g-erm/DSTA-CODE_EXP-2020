package com.example.powerpuffgirls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.ListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsActivity extends AppCompatActivity {
    private static final String TAG = "FriendsActivity";

    SearchView mySearchView;
    ListView myList;

    ArrayList<String> allId;
    ArrayList<String> allNames = new ArrayList<>();
    ArrayAdapter<String> adapter;

    public HashMap<String, String> namesAndId = new HashMap<>();

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mySearchView = (SearchView)findViewById(R.id.searchView);
        myList = (ListView)findViewById(R.id.friendsList);

        mDatabase.child("names").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getMap(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        if (!this.namesAndId.isEmpty()) {
            this.allNames = new ArrayList<>(this.namesAndId.keySet());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.allNames);
        myList.setAdapter(adapter);

        //String name = mySearchView.getQuery().toString();

        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void getMap(DataSnapshot dataSnapshot) {
        HashMap<String, String> currmap = (HashMap<String, String>) dataSnapshot.getValue();
        if (currmap != null) {
            this.namesAndId = currmap;
        } else {
            this.namesAndId = new HashMap<>();
        }
    }
}