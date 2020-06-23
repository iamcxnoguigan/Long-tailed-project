package com.l2l.enterprise.vessel.domain;

import java.io.Serializable;

public class Campaign implements Serializable{
    String name;
    String lang;

    public Campaign(){

    }

    public Campaign(String name, String lang){
        this.name=name;
        this.lang=lang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "name='" + name + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }
}
