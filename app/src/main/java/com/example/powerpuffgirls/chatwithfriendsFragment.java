package com.example.powerpuffgirls;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chatwithfriendsFragment extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String currUserId = mAuth.getUid();
    private FirebaseListAdapter<ChatMessage> adapter;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_chatwithfriends, container, false);
        FloatingActionButton fab =
                rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            private String name;

            @Override
            public void onClick(View view) {
                final String[] name = new String[1];
                EditText input = rootView.findViewById(R.id.input);

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        name[0] = (String) dataSnapshot.child(currUserId).child("profile").child("name").getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                mDatabase
                        .child("messages")
                        .setValue(new ChatMessage(input.getText().toString(), name[0]));

                // Clear the input
                input.setText("");
            }
        });
        displayChatMessages();
        return rootView;
    }

    private void displayChatMessages() {
        ListView listOfMessages = rootView.findViewById(R.id.list_of_messages);

        Query query = mDatabase.child("messages").orderByKey();

        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setLayout(R.layout.message)
                .setQuery(query, ChatMessage.class)
                .build();


//        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
//                R.layout.message, mDatabase) {
        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };
        listOfMessages.setAdapter(adapter);
    }

}