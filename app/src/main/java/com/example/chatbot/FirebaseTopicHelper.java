package com.example.chatbot;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseTopicHelper {
    FirebaseDatabase database;
    DatabaseReference ref;
    private List<Topic> themengebiet = new ArrayList<>();



    public FirebaseTopicHelper(boolean vorläufig) {
        database = FirebaseDatabase.getInstance();
        if(vorläufig == true){
            ref = database.getReference("vorläufigeHobbyzuordnung");
        }
        else {
            ref = database.getReference("Hobbyzuordnung");
        }
    }

    static Boolean topicRead = false;

    public void readThemengebiet(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                themengebiet.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keynote : dataSnapshot.getChildren()){
                    keys.add(keynote.getKey());
                    Topic thema = keynote.getValue(Topic.class);
                    themengebiet.add(thema);
                    // TEST CODE
                    topicRead = true;
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

    public List<Topic> getArrayList(){
        return themengebiet;
    }

    public String ListSucher(List<Topic>list, String gesuchtesWort ){
        String result = null;
        for (Topic ausgabe: list) {
            if(ausgabe.getHobby().equals(gesuchtesWort)){
                result = ausgabe.getThemengebiet();
                System.out.println("test1 hurra es funktioniert" + result);
                break;
            }
        }
        return result;
    }
}
