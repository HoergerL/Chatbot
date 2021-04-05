package com.example.chatbot;

import android.content.Intent;
import android.database.DataSetObserver;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

// TODO Überall Javadocs Kommentare hinzufügen /** ... */
// Nachlesen wie man sie zu formulieren hat!

public class MainActivity extends AppCompatActivity {

    String userMessage;
    ArrayList<String> hobbyList;
    ArrayList<String> subjectList;
    String word;
    int flagQuestion;
    List<String> answerQueue;
    int flagHobbies;
    List resultCoursesList;
    String topic;
    List<String> numbersList;
    String username;
    ArrayList<String> personalisedCourses;
    FirebaseTopicHelper topicHelper;
    FirebaseTopicHelper provisionalTopicHelper;
    FirebaseQuestionHelper questionHelper;
    FirebaseFachHelper subjectHelper;
    FirebaseFachHelper provisionalSubjectHelper;
    FirebaseCourseHelper courseHelper;
    FirebaseDatabase database;
    DatabaseReference hobbyAssignment;
    DatabaseReference provisionalHobbyAssignment;
    DatabaseReference subjectAssignment;
    DatabaseReference provisionalSubjectAssignment;
    List<Topic> topicDB;
    List<Fach> fachDB;
    List<Fragen> questionList;
    List resultTopic;
    List<String> questionsToAsk;
    private ChatArrayAdapter chatArrayAdapter;
    private ListView chatBubblesListView;
    private EditText chatET;
    private Button sendBTN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the username from the login page
        Intent i = getIntent();
        username = i.getStringExtra("username");
        if (username == null) {
            username = "Anonym";
        }
        username = username.trim();

        //initalizing Firebase variables to access and use Firebase-Database
        personalisedCourses = new ArrayList<>();
        resultTopic = new ArrayList();
        topicHelper = new FirebaseTopicHelper(false);
        provisionalTopicHelper = new FirebaseTopicHelper(true);
        questionHelper = new FirebaseQuestionHelper();
        subjectHelper = new FirebaseFachHelper(false);
        provisionalSubjectHelper = new FirebaseFachHelper(true);
        courseHelper = new FirebaseCourseHelper();

        //asynchronously access Firebase-Database entries
        topicHelper.readThemengebiet();
        System.out.println("### beginning"+FirebaseTopicHelper.topicRead);
        provisionalTopicHelper.readThemengebiet();
        questionHelper.readThemengebiet();
        questionList = questionHelper.getArrayList();
        subjectHelper.readThemengebiet();
        provisionalSubjectHelper.readThemengebiet();
        courseHelper.readStudiengang();

        //initializing connection to databases to insert data into it
        database = FirebaseDatabase.getInstance();
        hobbyAssignment = database.getReference("Hobbyzuordnung");
        provisionalHobbyAssignment = database.getReference("vorläufigeHobbyzuordnung");
        subjectAssignment = database.getReference("Fachzuordnung");
        provisionalSubjectAssignment = database.getReference("vorläufigeFachzuordnung");


        //setting flags
        flagQuestion = 1;
        flagHobbies = 0;


        //initalizing of the GUI
        sendBTN = findViewById(R.id.sendBTN);
        chatBubblesListView = findViewById(R.id.chatBubblesListView);
        chatET = findViewById(R.id.chatET);


        //creating the chatbubbles with new Input
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(),
                R.layout.activity_chat_singlemessage);
        chatBubblesListView.setAdapter(chatArrayAdapter);
        chatBubblesListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chatBubblesListView.setAdapter(chatArrayAdapter);


        //initializing of ArrayLists to put Inputs and Results in it
        numbersList = new ArrayList<>();
        resultCoursesList = new ArrayList();
        hobbyList = new ArrayList<>();
        subjectList = new ArrayList<>();


        //starting to chat
        answerQueue = new ArrayList<>();
        answerQueue.add("Hallo " + username + ", schön dich kennenzulernen. \uD83D\uDE0A");
        answerQueue.add("Ich heiße Charlie und werde dich dabei unterstützen, einen passenden " +
                "DHBW Studiengang auszuwählen.");
        answerQueue.add("Die Auswahl findet über eine Analyse deiner Interessen statt.");
        answerQueue.add("Deshalb gib nun bitte alle deine Hobbys durch ein Komma getrennt ein.");
        KINachrichtAuslösen();



       /* //onClick for Send-Button
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                message = chatText.getText().toString();

                //if there is no message and users presses send, nothing happens
                if (!(message.equals(""))) {
                    userSendChatMessage();
                    identifyMessage();
                }
            }
        });*/


        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                chatBubblesListView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    //causing the chatbubble of the KI to appear
    private void KISendChatMessage(final String aussage) {
        chatET.setEnabled(false);
        chatArrayAdapter.add(new ChatMessage(true, aussage));
        chatArrayAdapter.notifyDataSetChanged();
        chatET.setEnabled(true);
    }

    //if the users presses send button
    public void onClickSend(View view) {
        userMessage = chatET.getText().toString();

        //if there is no message and users presses send, nothing happens
        if (!(userMessage.equals(""))) {
            userSendChatMessage();
            identifyMessage();
        }
    }

    //causing the chatbubble of the user to appear and clearing the editText
    private void userSendChatMessage() {
        chatArrayAdapter.add(new ChatMessage(false, userMessage));
        chatET.setText("");
    }


    //identifying to what kind of question the user is answering
    private void identifyMessage() {

        //flagQuestion == 1 => answering his favorite hobbys
        if (flagQuestion == 1) {
            System.out.println("### flagQuestion = 1:"+FirebaseTopicHelper.topicRead);
            topicDB = topicHelper.getArrayList();

            splitWords(topicDB);
        }

        //flagQuestion == 2 => answering his favorite subjects
        else if (flagQuestion == 2) {

            fachDB = subjectHelper.getArrayList();
            splitWords(fachDB);
        }

        //flagQuestion == 3 => learning mode (learning a new hobby or subject)
        else if (flagQuestion == 3) {
            learning();
        }


        //flagQuestion ==4 => Question-Mode (finding the favorite activites of the user in the
        // chosen topics)
        else if (flagQuestion == 4) {
            splitWords(questionsToAsk);
        }
    }


    //splitting the input of the user by comma and transmitting it to checkAnswer-Method
    public void splitWords(List list) {
        final String[] topic;
        topic = (userMessage.split(","));

        if (flagQuestion == 1) {
            Collections.addAll(hobbyList, topic);
            checkAnswer(hobbyList, list);
        } else if (flagQuestion == 2) {
            Collections.addAll(subjectList, topic);
            checkAnswer(subjectList, list);
        } else if (flagQuestion == 4) {
            ending(topic);
        }
    }


    public boolean checkAnswer(ArrayList<String> list, List liste) {
        String result;
        Log.i("test1", String.valueOf(list.size()));
        while (!(list.isEmpty())) {
            result = null;
            System.out.println(list.get(0) + "test1###");
            word = list.remove(0);
            word = word.trim();


            DownloadTask task = new DownloadTask();
            DownloadTask task1 = new DownloadTask();
            String ergebnis = "";
            try {
                String klein = Character.toUpperCase(word.charAt(0)) + word.substring(1);
                ergebnis = task.execute("https://www.dwds.de/api/wb/snippet?q=" + klein).get();
                Log.i("test1erg deutschgroß", ergebnis);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }


            word = word.toLowerCase();


            if (ergebnis.equals("[]")) {
                try {
                    ergebnis = task1.execute("https://www.dwds.de/api/wb/snippet?q=" + word).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                Log.i("test1ergdeutschklein", ergebnis);
            }

            if (ergebnis.contains("[{")) {
                Log.i("test1", "result ist nicht null");
            } else {
                Log.i("test1keindeutsches Wort", word);
                answerQueue.add("Leider kenne ich das Wort " + word + " nicht. Kann es sein, dass" +
                        " es einen Rechtschreibfehler beinhaltet? Du musst wissen, ich lege " +
                        "wirklich sehr viel Wert auf eine korrekte Schreibweise. Dazu zählt auch " +
                        "Groß- und Kleinschreibung \uD83D\uDE09");
                answerQueue.add("Achja.... in Fremdsprachen bin ich leider gar nicht gut. Das " +
                        "Wort muss daher auf deutsch sein.");
                answerQueue.add("bitte gib das Wort nochmals verbessert ein");
                KINachrichtAuslösen();
                Log.i("KI Nachricht auslösen", "4");
                return false;
            }


            if (flagQuestion == 1) {
                result = topicHelper.ListSucher(liste, word);
            } else if (flagQuestion == 2) {
                result = subjectHelper.ListSucher(liste, word);

            }


            if (result == null) {
                answerQueue.add("Ich kenne " + word + " nicht.");
                if (flagQuestion == 2) {
                    topic = "Schulfach";
                } else {
                    topic = "Hobby";
                }
                answerQueue.add("Kannst du das " + topic + " einem der folgenden Themen " +
                        "zuordnen?\n" +
                        "Gesundheit,\n" + "IT,\n" + "Kreativität,\n" + "Planen,\n" + "Social " +
                        "Media,\n" + "sozial,\n" +
                        "Sport,\n" + "Wirtschaft,\n" + "Naturwissenschaft,\n" + "Sprache\n\n" +
                        "bitte antworte mir mit dem passenden Themengebiet. Falls dein " + topic + " zu keinem der Themengebiete passt, so antworte bitte mit nein.");
                KINachrichtAuslösen();
                Log.i("KI Nachricht auslösen", "2");


                flagQuestion = 3;
                return false;


            } else {
                Log.i("Thema", "Wort: " + word + "Themengebiet" + result);
                ThemaHinzufügen(result);
            }


        }
        KINachrichtAuslösen();
        Log.i("KI Nachricht auslösen", "5");

        if (flagQuestion == 1) {
            flagQuestion = 2;
            flagHobbies++;
            answerQueue.add("Als nächstes würde ich gerne wissen, was dein Lieblingsschulfach war" +
                    ". Auch hier kannst du gerne mehrere durch ein Komma getrennt aufzählen.");

        } else if (flagQuestion == 2) {
            flagQuestion = 4;
            List<String> duplikatezuStellendeFragen = new ArrayList<String>();
            questionsToAsk = new ArrayList<String>();
            duplikatezuStellendeFragen = questionHelper.fragenSucher(questionList, resultTopic);
            System.out.println("test1 duplikate zus tellendene Fragen" + duplikatezuStellendeFragen.toString() + "Länge" + duplikatezuStellendeFragen.size());
            String ausgabeErgebnis = "";
            for (int i = 0; i < resultCoursesList.size(); i++) {
                if (i == (resultCoursesList.size() - 1)) {
                    ausgabeErgebnis = ausgabeErgebnis + resultCoursesList.get(i);
                } else {
                    ausgabeErgebnis = ausgabeErgebnis + resultCoursesList.get(i) + ",\n";
                }

            }
            String ausgabeFragen = "";

            //prüfen, ob die Tätigkeit bereits in der Ausgabe enthalten ist
            for (int i = 0; i < duplikatezuStellendeFragen.size(); i++) {
                if (!(questionsToAsk.contains(duplikatezuStellendeFragen.get(i)))) {
                    questionsToAsk.add(duplikatezuStellendeFragen.get(i));
                }
            }

            System.out.println("test1 zus tellendene Fragen" + questionsToAsk.toString() + "Länge"
                    + questionsToAsk.size());

            for (int i = 1; i <= questionsToAsk.size(); i++) {
                ausgabeFragen = ausgabeFragen + i + ": " + questionsToAsk.get(i - 1) + "\n";
            }
            System.out.println("ausgabe Ereignis#####" + ausgabeErgebnis);
            answerQueue.add("Basierend auf deinen Hobbys und Lieblingsfächern könnten dich " +
                    "folgendene Tätigkeiten interessieren: \n\n" + ausgabeFragen + "\nBitte " +
                    "antworte mit den Nummern der Tätigkeiten, die dich interessieren. Trenne " +
                    "diese bitte mit einem Komma. Gib dabei bitte das Themengebiet, dass dich am " +
                    "meisten interessiert als erstes an");

        }


        KINachrichtAuslösen();
        Log.i("KI Nachricht auslösen", "6");
        System.out.println("test1" + resultTopic.toString());
        return true;
    }


    public void learning() {

        if (userMessage.toLowerCase().equals("nein")) {
            if (topic.equals("Schulfach")) {
                answerQueue.add("Wir werden dein Schulfach " + word + " individuell prüfen");
                flagQuestion = 2;
                checkAnswer(subjectList, fachDB);
            } else if (topic.equals("Hobby")) {
                answerQueue.add("Wir werden dein hobby " + word + " individuell prüfen");
                flagQuestion = 1;
                checkAnswer(hobbyList, topicDB);
            }

        } else {


            userMessage = userMessage.toLowerCase().trim();
            Log.i("test1 eingabe",
                    userMessage + "Länge fragenListarray" + String.valueOf(questionList.size()));

            String themaexistiert = questionHelper.ListSucher(questionList, userMessage);
            Log.i("test1",
                    "eingegebenes Thema existiert grundsätlzich aus DB: " + themaexistiert + " " +
                            "eingabe " + userMessage);


            if (!(themaexistiert == null)) {
                //überprüfen, ob eines der Themengebiete genannt wurde, die wir festgelegt haben
                String hobbyexistiert = "";
                //wenn der Eintrag bereits in der vorläufigen Tabelle existiert, dann soll er in
                // die normale Tabelle übertragen werden
                if (topic.equals("Hobby")) {
                    List<Topic> previousSubjectList = provisionalTopicHelper.getArrayList();
                    hobbyexistiert = provisionalTopicHelper.ListSucher(previousSubjectList, word);
                } else if (topic.equals("Schulfach")) {
                    List<Fach> vorläufigFachlist = provisionalSubjectHelper.getArrayList();
                    hobbyexistiert = provisionalSubjectHelper.ListSucher(vorläufigFachlist, word);
                }
                Log.i("test1", "thema aus DB: " + hobbyexistiert + "eingabe" + userMessage);

                //das eingegebene Themenegebiet wird verwendet, um die Zuordnung zu machen
                ThemaHinzufügen(userMessage);

                //wenn das hobby bereits zuvor eingetragen wurde, dann wird überprüft, ob es mit
                // dem gleichen Themengebiet eingefügt wurde
                //falls ja, wird es in die normale Tabelle übertragen. Der eintrag aus der
                // vorläufigen Tabelle wird in jedem Fall gelöscht
                //wurde das Hobby noch nie eingetragen oder wieder gelöscht, wird es in die
                // vorläufige Tabelle eingetragen
                //verhindert verfälschungen durch gezielte falsche Zuordnung oder verfälschung
                // Ergebnis, falls das Hobby nicht zugeordnet werden kann(passt zu keinem der
                // Themengebiete)
                if (!(hobbyexistiert == null)) {
                    Log.i("test1", "thema: " + hobbyexistiert + "hobby: " + word);
                    if (topic.equals("Hobby")) {
                        if (hobbyexistiert.equals(userMessage)) {
                            HilfsklasseThemengebiet hilf1 =
                                    new HilfsklasseThemengebiet(hobbyexistiert, word);
                            hobbyAssignment.child(word).setValue(hilf1);
                        }
                        löschenVorläufiges(word);
                        flagQuestion = 1;
                        checkAnswer(hobbyList, topicDB);
                    } else if (topic.equals("Schulfach")) {
                        if (hobbyexistiert.equals(userMessage)) {
                            HilfsklasseFach hilf1 = new HilfsklasseFach(hobbyexistiert, word);
                            subjectAssignment.child(word).setValue(hilf1);
                        }
                        löschenVorläufiges(word);
                        flagQuestion = 2;
                        checkAnswer(subjectList, fachDB);
                    } else {
                        Log.i("test1", "SCHREIBEN IN DIE NORMALE DB FEHLGESCHLAGEN");
                    }


                } else {
                    if (topic.equals("Hobby")) {
                        Log.i("test1else", "thema: " + hobbyexistiert + "hobby: " + word);
                        HilfsklasseThemengebiet hilf1 =
                                new HilfsklasseThemengebiet(userMessage.toLowerCase(), word);
                        provisionalHobbyAssignment.child(word).setValue(hilf1);

                        flagQuestion = 1;
                        checkAnswer(hobbyList, topicDB);
                    } else if (topic.equals("Schulfach")) {
                        Log.i("test1else", "thema: " + hobbyexistiert + "Schulfach: " + word);
                        Fach hilf1 = new Fach(word, userMessage.toLowerCase());
                        provisionalSubjectAssignment.child(word).setValue(hilf1);

                        flagQuestion = 2;
                        checkAnswer(subjectList, fachDB);
                    }
                }


            } else {
                answerQueue.add("bitte überprüfe deine Eingabe, du hast mit keinem der " +
                        "angegebenen Themengebiete geantwortet ");
                KINachrichtAuslösen();
            }
        }
    }

    public boolean KINachrichtAuslösen() {

        final int längeQueue = answerQueue.size() + 2;

        new CountDownTimer(3000 * längeQueue, 3000) {

            @Override
            public void onTick(long millisUntilFinished) {

                if (millisUntilFinished < 3000 * längeQueue - 3000 && !answerQueue.isEmpty()) {
                    // System.out.println("On Tick #### " + antwortenqueue.get(0));
                    audioErzeugen();
                    KISendChatMessage(answerQueue.remove(0));
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();


        return true;
    }

    public void audioErzeugen() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.audio);
        mediaPlayer.start();
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

    public void löschenVorläufiges(String nameHobby) {
        if (topic.equals("Hobby")) {
            provisionalHobbyAssignment.child(nameHobby).removeValue();
        } else if (topic.equals("Schulfach")) {
            provisionalSubjectAssignment.child(nameHobby).removeValue();
        } else {
            Log.i("test1", "FEHLERlöschen vorläufiges");
        }
    }

    public void ThemaHinzufügen(String thema) {
        if (!(resultTopic.contains(thema))) {
            resultTopic.add(thema);
        }
    }

    public void onClickErgebnisbutton(View view) {
        Intent intent = new Intent(this, OverviewCourses.class);
        intent.putExtra("personalisierteStudiengänge", personalisedCourses);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }


    public void ending(String[] topic) {
        System.out.println("test1 flag frage ==4");

        Collections.addAll(numbersList, topic);


        int anzahlAntworten = questionsToAsk.size();
        List<Studiengang> studiengänge = courseHelper.getArrayList();

        int eingabe = 0;
        try {
            while (!(numbersList.isEmpty())) {

                eingabe = Integer.parseInt(numbersList.remove(0).trim());
                Log.i("test eingegebeneZahl", String.valueOf(eingabe));
                if (eingabe == 0 || eingabe > anzahlAntworten) {
                    throw new NumberFormatException();

                } else {


                    String studiengang = courseHelper.ListSucher(studiengänge,
                            questionsToAsk.get(eingabe - 1));
                    if (!personalisedCourses.contains(studiengang))
                        personalisedCourses.add(studiengang);

                    System.out.println("test1 personalisierteStudiengänge" + personalisedCourses.toString());

                    Log.i("test1ausgewählteFrage",
                            questionsToAsk.get(eingabe - 1) + String.valueOf(eingabe - 1));

                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.i("test1Exception", "exception wird geworfen");
            answerQueue.add("deine Eingabe " + eingabe + " ist keine der geforderdeten Zahlen, " +
                    "bitte gib die Zahl ein, die du eigentlich gemeint hast");
            KINachrichtAuslösen();
            Log.i("KI Nachricht auslösen", "2");
            return;
        }

        Log.i("test1", "HURRA es ist komplett durchgelaufen");
        answerQueue.add("Vielen Dank für die nette Unterhaltung. Das Ergebnis der Auswertung " +
                "erhältst du, wenn du auf den roten Button klickst. Ich hoffe, ich konnte dir " +
                "weiterhelfen! \uD83D\uDE0A");
        KINachrichtAuslösen();
        Log.i("KI Nachricht auslösen", "3");
        sendBTN.animate().alpha(0).setDuration(3000);
        chatET.animate().alpha(0).setDuration(3000);
        new CountDownTimer(3500, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                sendBTN.setVisibility(View.GONE);
                chatET.setVisibility(View.GONE);
                Button ergebnisbutton = findViewById(R.id.resultBTN);
                ergebnisbutton.setVisibility(View.VISIBLE);

            }
        }.start();
    }


}
