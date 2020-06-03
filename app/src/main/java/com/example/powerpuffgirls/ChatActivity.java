package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new FriendsListFragment(), "Friends");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    public void gotoFriend(View view) {
        startActivity(new Intent(ChatActivity.this, FriendsActivity.class));
    }
}