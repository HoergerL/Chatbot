package com.example.chatbot;

public class HilfsklasseFach {
    String themengebiet;
    String fach;

    public HilfsklasseFach() {
    }

    public HilfsklasseFach(String themengebiet, String fach) {
        this.themengebiet = themengebiet;
        this.fach = fach;
    }

    public String getThemengebiet() {
        return themengebiet;
    }

    public void setThemengebiet(String themengebiet) {
        this.themengebiet = themengebiet;
    }

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }
}
