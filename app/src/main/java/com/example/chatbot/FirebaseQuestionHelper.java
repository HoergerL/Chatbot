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

public class FirebaseQuestionHelper {


        FirebaseDatabase database;
        DatabaseReference ref;
        private List<Fragen> questions = new ArrayList<>();



        public FirebaseQuestionHelper() {
            database = FirebaseDatabase.getInstance();
            ref = database.getReference("fragenzuordnung");

        }

        public void readThemengebiet(){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    questions.clear();
                    List<String> keys = new ArrayList<>();
                    for(DataSnapshot keynote : dataSnapshot.getChildren()){
                        keys.add(keynote.getKey());
                        Fragen frage = keynote.getValue(Fragen.class);
                        questions.add(frage);

                    }
                    /*
                    int i = 0;
                    for (Fragen ausgabe: fragen
                    ) {

                        i++;
                        System.out.println("test1: " + i + ausgabe.getThemengebiet() + ausgabe.getFragen());
                        if (ausgabe.getThemengebiet().equals("gesundheit")){
                            String [] arrayFragen= ausgabe.getFragen();
                            System.out.println("test1: " + arrayFragen[1]);
                            break;
                        }

                    }*/

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public List<Fragen> getArrayList(){
            return questions;
        }

        public String ListSucher(List<Fragen>list, String gesuchtesWort ){
            String result = null;
            for (Fragen ausgabe: list) {
                //Log.i("test1Schleife", ausgabe.getThemengebiet());
                if(ausgabe.getThemengebiet().equals(gesuchtesWort)){
                    result = ausgabe.getThemengebiet();
                    System.out.println("test1 hurra das Themengebiet existiert, es funktioniert" + result);
                    break;
                }
            }
            return result;
        }
        public List<String> fragenSucher(List<Fragen>list, List<String> gesuchteWörter){
            List<String> result = new ArrayList<String>();
            while (!(gesuchteWörter.isEmpty())){
                String gesuchtesWort = gesuchteWörter.remove(0);
                Log.i("test1", "gesuchtes Wort" + gesuchtesWort);
                for (Fragen ausgabe: list) {
                    //String [] arrayfragen = fragen.split("\\?");
                    int j = 0;
                    if (ausgabe.getThemengebiet().equals(gesuchtesWort)) {
                        String ergebnis = ausgabe.getFragen();
                        String[] ergebnisse = ergebnis.split("\\?");
                        Log.i("test1länge ergebnisse", String.valueOf(ergebnisse.length));
                        for (j = 0; j<ergebnisse.length;j++) {
                            Log.i("test1 an der STelle j" , ergebnisse[j]);
                            result.add(ergebnisse[j]);

                        }
                        System.out.println("test1 hurra es funktioniert" + result.get(0));
                    }
                }
            }
            return result;

    }


}
