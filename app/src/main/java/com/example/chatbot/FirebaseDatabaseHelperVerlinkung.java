package com.example.chatbot;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelperVerlinkung {
    FirebaseDatabase database;
    DatabaseReference ref;
    private List<WebLink> links = new ArrayList<>();



    public FirebaseDatabaseHelperVerlinkung() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Verlinkung");

    }

    public void readThemengebiet(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                links.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keynote : dataSnapshot.getChildren()){
                    keys.add(keynote.getKey());
                    WebLink link = keynote.getValue(WebLink.class);
                    links.add(link);

                } /*
                int i = 0;
                for (Themengebiet ausgabe: themengebiet
                ) {

                    i++;
                    System.out.println("test1: " + i + ausgabe.getThemengebiet() + ausgabe.getHobby());
                    if (ausgabe.getHobby().equals("rechnen")){
                        System.out.println("test1: " + ausgabe.getThemengebiet());
                    }

                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<WebLink> getArrayList(){
        return links;
    }

    public String ListSucher(List<WebLink>list, String gesuchtesWort ){
        String result = null;
        //Log.i("test1gesuchtes Wort", gesuchtesWort);
        //System.out.println("test1liste" + list.toString());

        for (WebLink ausgabe: list) {
            if(ausgabe.getThemengebiet().equals(gesuchtesWort)){
                result = ausgabe.getLink();
                System.out.println("test1 hurra es funktioniert" + result);
                break;
            }
        }
        return result;
    }
}
