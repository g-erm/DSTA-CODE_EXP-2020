package com.example.powerpuffgirls;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Predicate;

public class FriendsActivity extends AppCompatActivity {
    private static final String TAG = "FriendsActivity";

    SearchView mySearchView;
    ListView myList;

    ArrayAdapter<String> adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String currUserId;

    private int removeCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currUserId = mAuth.getUid();

        mySearchView = findViewById(R.id.searchView);
        myList = findViewById(R.id.friendsList);

        getData(new OnGetDataListener() {

            @Override
            public void onSuccess(DataSnapshot dataSnapshot, ArrayList<String> name, ArrayList<String> id, ArrayList<String> friends) {
                editList(name, id, friends);
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
        final String currUser = this.currUserId;
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> names = (ArrayList<String>) dataSnapshot.child("names").getValue();
                ArrayList<String> ids = (ArrayList<String>) dataSnapshot.child("ids").getValue();
                ArrayList<String> friends = (ArrayList<String>) dataSnapshot.child("users").child(currUser).child("friends").getValue();
                while (names == null && ids == null && friends == null) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                listener.onSuccess(dataSnapshot, names, ids, friends);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public void editList(ArrayList<String> names, ArrayList<String> ids, ArrayList<String> friends) {
        int mypos = ids.indexOf(currUserId);
        names.remove(mypos);
        ids.remove(mypos);
        int size = friends.size();
        if (size > 1) {
            for (String id : friends) {
                if (ids.contains(id)) {
                    Log.d("curr id", id);
                    int pos = ids.indexOf(id);
                    Log.d("index of val", Integer.toString(pos));
                    ids.remove(pos);
                    names.remove(pos);
                    this.removeCount++;
                }
            }
        }
        Log.d("names list", names.toString());
        createList(names, ids, friends);
    }

    public void createList(ArrayList<String> names, ArrayList<String> ids, ArrayList<String> friends) {
        myList.setAdapter(new CustomAdapter(names, ids, friends,FriendsActivity.this));
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

    class CustomAdapter extends BaseAdapter implements ListAdapter {
        private ArrayList<String> namelist = new ArrayList<String>();
        private ArrayList<String> idlist = new ArrayList<String>();
        private ArrayList<String> friendslist = new ArrayList<>();
        private Context context;

        public CustomAdapter(ArrayList<String> namelist, ArrayList<String> idlist, ArrayList<String> friendslist, Context context) {
            this.namelist = namelist;
            this.idlist = idlist;
            this.friendslist = friendslist;
            this.context = context;
        }

        @Override
        public int getCount() {
            return namelist.size();
        }

        @Override
        public Object getItem(int pos) {
            return namelist.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
            //just return 0 if your list items do not have an Id variable.
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.customlayout, null);
            }

            //Handle TextView and display string from your list
            TextView listOfPeople= view.findViewById(R.id.listOfPeople);
            Log.d("position", Integer.toString(position));
            Log.d("remove count", Integer.toString(removeCount));
            final String userid;
            listOfPeople.setText(namelist.get(position));
            userid = idlist.get(position);

            //Handle buttons and add onClickListeners
            Button addbtn= view.findViewById(R.id.btn);

            addbtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    updateFriendsList(userid, friendslist);
                    Button button = (Button)v;
                    button.setVisibility(View.INVISIBLE);
                }
            });
            return view;
        }
    }

    public void updateFriendsList(String userid, ArrayList<String> friendslist) {
        friendslist.add(userid);
        mDatabase.child("users").child(this.currUserId).child("friends").setValue(friendslist);
    }
}