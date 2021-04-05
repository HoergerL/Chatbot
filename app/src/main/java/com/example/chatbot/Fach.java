package com.example.chatbot;

public class Fach {
    private String fach;
    private String themengebiet;

    public Fach(String fach, String themengebiet) {
        this.fach = fach;
        this.themengebiet = themengebiet;
    }

    public Fach() {
    }

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }

    public String getThemengebiet() {
        return themengebiet;
    }

    public void setThemengebiet(String themengebiet) {
        this.themengebiet = themengebiet;
    }
}
