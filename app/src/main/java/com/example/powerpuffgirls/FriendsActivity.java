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
    ArrayList<String> allNames;
    ArrayAdapter<String> adapter;

    public HashMap<String, String> namesAndId = new HashMap<>();

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mySearchView = (SearchView)findViewById(R.id.searchView);
        myList = (ListView)findViewById(R.id.friendsList);

//        allNames.add("bob");
//        allNames.add("sally");
//        allNames.add("parry");
//        allNames.add("alicia");
//        allNames.add("amelia");
//        allNames.add("fiona");
//        allNames.add("cheryl");
//        allNames.add("jolene");
//        allNames.add("megan");

        mDatabase.child("names").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getExistingData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        ArrayList<String> names = new ArrayList<>();
        if (this.allNames == null) {
            this.allNames = new ArrayList<>();
        } else {
            for (String s : this.allNames) {
                names.add(s);
            }
        }

        System.out.println(names);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        myList.setAdapter(adapter);

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

    private void getExistingData(DataSnapshot dataSnapshot) {
        this.allNames = (ArrayList<String>) dataSnapshot.child("names").getValue();
        this.allId = (ArrayList<String>) dataSnapshot.child("ids").getValue();
        System.out.println(this.allNames);
    }

}