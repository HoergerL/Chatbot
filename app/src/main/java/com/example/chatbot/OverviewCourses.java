package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class OverviewCourses extends AppCompatActivity {
    Button Send;
    String username;
    //EditText editText;
    ArrayList<String> studiengänge;
    FirebaseDatabaseHelperVerlinkung linkhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uebersicht_studiengaenge);
        Intent i = getIntent();
        username = i.getStringExtra("username");
        if(username== null){
            username = "anonym";
        }
        studiengänge = i.getStringArrayListExtra("personalisierteStudiengänge");
        studiengänge.remove(null);
        Log.i("test1username", username);
        ListView list = findViewById(R.id.list);
        linkhelper = new FirebaseDatabaseHelperVerlinkung();
        linkhelper.readThemengebiet();
        //studiengänge = new ArrayList<String>();
        /*studiengänge.add("BWL-Dienstleistungsmanagement Schwerpunkt Non-Profit-Organisationen, Verbände und Stiftungen");
        studiengänge.add("BWL-Dienstleistungsmanagement Schwerpunkt Consulting & Sales");
        studiengänge.add("BWL-Dienstleistungsmanagement Schwerpunkt Media, Vertrieb und Kommunikation (MuK)");
        studiengänge.add("BWL-Dienstleistungsmanagement Schwerpunkt Logistik- und Supply Chain Management");
        studiengänge.add("BWL-Dienstleistungsmanagement Schwerpunkt International Services Management"); */
        System.out.println("test1"+ studiengänge.toString());


         ArrayAdapter <String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, studiengänge);

         list.setAdapter(arrayAdapter);



         list.setOnItemClickListener( new AdapterView.OnItemClickListener(){

             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 String ausgewählterStudiengang = studiengänge.get(position);
                 List<WebLink> links = linkhelper.getArrayList();
                 String result = linkhelper.ListSucher(links,ausgewählterStudiengang);
                 Log.i("test1result", result);

                 Intent i = new Intent(getApplicationContext(), Link.class);
                 i.putExtra("link", result);
                 startActivity(i);

             }
         });

        }

    public void beenden(View view){
        Log.i("test1", "beenden wird ausgeführt");
        //finishAndRemoveTask();
        android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(1);
    }

    public void restart(View view){
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("username", username);
        startActivity(i);
        finish();
    }

    public void dhbwseite(View view){
        Intent i = new Intent(this, Link.class);
        i.putExtra("link", "https://www.dhbw-stuttgart.de/home/");
        startActivity(i);
        //finish();
    }
    public void kontakt(View view){
        Intent i = new Intent(this, Contact.class);
        startActivity(i);

    }



    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.testlogout, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        Toast.makeText(this, "ausgeloggt", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
        return true;

    }

    }













