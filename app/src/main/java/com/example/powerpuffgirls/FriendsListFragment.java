package com.example.powerpuffgirls;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsListFragment extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String currUserId = mAuth.getUid();

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.friends_list_fragment, container, false);

        getData(new OnGetDataListener() {

            @Override
            public void onSuccess(DataSnapshot dataSnapshot, ArrayList<String> name, ArrayList<String> id, ArrayList<String> friends) {
                ArrayList<String> friendsNames = adjustIdsToNames(name, id, friends);
                ListView listView = (ListView)rootView.findViewById(R.id.myfriends);
                adapt(listView, friendsNames);
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

        return rootView;
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
                Log.d("first friends list", friends.toString());
                listener.onSuccess(dataSnapshot, names, ids, friends);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public ArrayList<String> adjustIdsToNames(ArrayList<String> names, ArrayList<String> ids, ArrayList<String> friendsIds) {
        ArrayList<String> friendsNames = new ArrayList<>();
        int size = friendsIds.size();
        for (String s : friendsIds.subList(1, size)) {
            int pos = ids.indexOf(s);
            friendsNames.add(names.get(pos));
        }
        return friendsNames;
    }

    public void adapt(ListView listView, ArrayList<String> friends) {
        listView.setAdapter(new ListViewAdapter(getActivity(), friends));
    }
}

class ListViewAdapter extends BaseAdapter {
    private static ArrayList<String> myfriends;
    private Context context;

    public ListViewAdapter(Context context, ArrayList<String> myfriends) {
        this.myfriends = myfriends;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.myfriends.size();
    }

    @Override
    public Object getItem(int arg0) {
        return this.myfriends.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.friendslistlayout, null);
        }

        TextView listOfFriends = (TextView) view.findViewById(R.id.listOfFriends);
        listOfFriends.setText(myfriends.get(position));
        listOfFriends.setGravity(Gravity.CENTER_VERTICAL);

        return view;
    }
}