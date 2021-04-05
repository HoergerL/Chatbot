package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class Contact extends AppCompatActivity {
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontakt);
        /*Intent i = getIntent();
        username = i.getStringExtra("username");*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    public void dhbwseite(View view){
        Intent i = new Intent(this, Link.class);
        i.putExtra("username", username);
        startActivity(i);
        //finish();
    }
}
