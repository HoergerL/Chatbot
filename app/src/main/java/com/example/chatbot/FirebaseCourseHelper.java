package com.example.chatbot;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseCourseHelper {
    FirebaseDatabase database;
    DatabaseReference ref;
    private List<Studiengang> studiengänge = new ArrayList<>();



    public FirebaseCourseHelper() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Studiengangzuordnung");
    }

    public void readStudiengang(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studiengänge.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keynote : dataSnapshot.getChildren()){
                    keys.add(keynote.getKey());
                    Studiengang studiengang = keynote.getValue(Studiengang.class);
                    studiengänge.add(studiengang);

                }

                    /*int i = 0;
                    for (Studiengang ausgabe: studiengänge
                    ) {
                        i++;
                        System.out.println("test1: " + i + ausgabe.getFrage() + ausgabe.getStudiengang());
                    }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<Studiengang> getArrayList(){
        return studiengänge;
    }

    public String ListSucher(List<Studiengang>list, String gesuchtesWort ){
        String result = null;
        Log.i("test1 List sucher", gesuchtesWort);
        for (Studiengang ausgabe: list) {
            //Log.i("test1Schleife", ausgabe.getFach()+ gesuchtesWort+" gesuhtes Wort");
            if(ausgabe.getFrage().equals(gesuchtesWort)){
                result = ausgabe.getStudiengang();
                System.out.println("test1 hurra das Themengebiet existiert, es funktioniert" + result);
                break;
            }
        }
        return result;
    }
}
