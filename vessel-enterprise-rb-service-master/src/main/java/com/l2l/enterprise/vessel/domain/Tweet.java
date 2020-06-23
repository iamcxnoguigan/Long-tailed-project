package com.l2l.enterprise.vessel.domain;

import java.io.Serializable;

public class Tweet implements Serializable {

    private String text;
    private String author;
    private String lang;
    private long timestamp;

    public Tweet() {
    }

    public Tweet(String text,
                 String author,
                 String lang,
                 long timestamp) {
        this.text = text;
        this.author = author;
        this.lang = lang;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public String getLang() {
        return lang;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", lang='" + lang + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
