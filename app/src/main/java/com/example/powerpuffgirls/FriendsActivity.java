package com.example.powerpuffgirls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.ListView;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.callback.Callback;

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

        getData(new OnGetDataListener() {

            @Override
            public void onSuccess(DataSnapshot dataSnapshot, ArrayList<String> name, ArrayList<String> id) {
                createList(name);
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
    }

    public void getData(final OnGetDataListener listener) {
        listener.onStart();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> names = (ArrayList<String>) dataSnapshot.child("names").getValue();
                ArrayList<String> ids = (ArrayList<String>) dataSnapshot.child("ids").getValue();
                while (names == null && names.isEmpty()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                listener.onSuccess(dataSnapshot, names, ids);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public void createList(ArrayList<String> names) {

        Log.d("list of names", names.toString());

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

}