package com.example.chatbot;

public class HilfsklasseThemengebiet {
    String hobby;
    String themengebiet;

    public HilfsklasseThemengebiet() {
    }

    public HilfsklasseThemengebiet(String themengebiet, String hobby) {
        this.hobby = hobby;
        this.themengebiet = themengebiet;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getThemengebiet() {
        return themengebiet;
    }

    public void setThemengebiet(String themengebiet) {
        this.themengebiet = themengebiet;
    }
}
