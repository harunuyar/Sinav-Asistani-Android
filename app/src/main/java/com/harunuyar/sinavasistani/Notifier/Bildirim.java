package com.harunuyar.sinavasistani.Notifier;

public class Bildirim {
    private String[] texts;

    public Bildirim(String[] texts) {
        this.texts = texts;
    }

    public String[] getTexts() {
        return texts;
    }

    public void setTexts(String[] texts) {
        this.texts = texts;
    }

    @Override
    public String toString() {
        String str = texts.toString();
        return str;
    }
    
}
