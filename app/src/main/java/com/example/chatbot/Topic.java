package com.example.chatbot;

public class Topic {
    private String hobby;
    private String themengebiet;

    public Topic() {
    }

    public Topic(String hobby, String thema) {
        this.hobby = hobby;
        this.themengebiet = thema;
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
