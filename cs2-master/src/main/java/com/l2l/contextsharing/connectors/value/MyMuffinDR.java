package com.l2l.contextsharing.connectors.value;

import java.util.List;

public class MyMuffinDR {
    double df;
    double rc;

    public MyMuffinDR() {
    }

    public void SetallVar(List<Integer> list) {
        int loc = list.get(0);
        int time = list.get(1);
        this.DF(loc, time);
        this.RC(time);
    }

    public double getDf() {
        return df;
    }

    public void setDf(double df) {
        this.df = df;
    }

    public double getRc() {
        return rc;
    }

    public void setRc(double rc) {
        this.rc = rc;
    }

    public void DF(int loc, int time){
        this.setDf(loc*10 + time);
    }

    public void RC(int time){
        this.setRc(time*0.75);
    }
}
