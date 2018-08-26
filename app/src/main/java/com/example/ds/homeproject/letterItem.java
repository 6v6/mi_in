package com.example.ds.homeproject;


public class letterItem {
    private String tv, letter;

    public letterItem(String tv, String letter) {
        if(tv.substring(0,1).equals("s"))
            this.tv = "보낸 편지";
        else if(tv.substring(0,1).equals("r"))
            this.tv = "받은 편지";
        this.letter = letter;
    }


    public String getTv() {
        return tv;
    }

    public String getLetter() {
        return letter;
    }
}
