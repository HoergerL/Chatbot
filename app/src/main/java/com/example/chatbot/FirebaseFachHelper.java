package com.example.chatbot;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseFachHelper {
    FirebaseDatabase database;
    DatabaseReference ref;
    private List<Fach> fächer = new ArrayList<>();



    public FirebaseFachHelper(boolean vorläufig) {
        database = FirebaseDatabase.getInstance();
        if(vorläufig == true){
            ref = database.getReference("vorläufigeFachzuordnung");
        }
        else {
            ref = database.getReference("Fachzuordnung");
        }
    }

    public void readThemengebiet(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fächer.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keynote : dataSnapshot.getChildren()){
                    keys.add(keynote.getKey());
                    Fach fach = keynote.getValue(Fach.class);
                    fächer.add(fach);

                }

                    /*int i = 0;
                    for (Fach ausgabe: fächer
                    ) {

                        i++;
                        System.out.println("test1: " + i + ausgabe.getThemengebiet() + ausgabe.getFach());
                        if (ausgabe.getThemengebiet().equals("wirtschaft")){
                            String arrayFragen= ausgabe.getThemengebiet();
                            System.out.println("test1: " + arrayFragen);
                            break;
                        }

                    } */

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<Fach> getArrayList(){
        return fächer;
    }

    public String ListSucher(List<Fach>list, String gesuchtesWort ){
        String result = null;
        for (Fach ausgabe: list) {
            //Log.i("test1Schleife", ausgabe.getFach()+ gesuchtesWort+" gesuhtes Wort");
            if(ausgabe.getFach().equals(gesuchtesWort)){
                result = ausgabe.getThemengebiet();
                System.out.println("test1 hurra das Themengebiet existiert, es funktioniert" + result);
                break;
            }
        }
        return result;
    }
}
