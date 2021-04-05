package com.example.chatbot;

public class Fragen {
    private String themengebiet;
    private String fragen;

    public String getThemengebiet() {
        return themengebiet;
    }

    public Fragen(String themengebiet, String fragen) {
        this.themengebiet = themengebiet;
        this.fragen = fragen;
    }

    public Fragen() {
    }

    public void setThemengebiet(String themengebiet) {
        this.themengebiet = themengebiet;
    }

    public String getFragen() {
        return fragen;
    }

    public void setFragen(String fragen) {
        this.fragen = fragen;
    }
}
