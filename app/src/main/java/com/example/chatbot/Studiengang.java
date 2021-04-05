package com.example.chatbot;

public class Studiengang {
    String frage;
    String studiengang;

    public Studiengang() {
    }

    public Studiengang(String studiengang, String frage) {
        this.frage = frage;
        this.studiengang = studiengang;
    }

    public String getFrage() {
        return frage;
    }

    public void setFrage(String frage) {
        this.frage = frage;
    }

    public String getStudiengang() {
        return studiengang;
    }

    public void setStudiengang(String studiengang) {
        this.studiengang = studiengang;
    }
}
