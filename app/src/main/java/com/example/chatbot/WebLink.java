package com.example.chatbot;

public class WebLink {
    String themengebiet;
    String link;

    public WebLink(String studiengang, String link) {
        this.themengebiet = studiengang;
        this.link = link;
    }

    public WebLink() {
    }

    public String getThemengebiet() {
        return themengebiet;
    }

    public void setThemengebiet(String themengebiet) {
        this.themengebiet = themengebiet;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
