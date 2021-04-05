package com.example.chatbot;

public class HilfsklasseVerlinkung {
    private String themengebiet;
    private String link;

    public HilfsklasseVerlinkung() {
    }

    public HilfsklasseVerlinkung(String themengebiet, String link) {
        this.themengebiet = themengebiet;
        this.link = link;
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
