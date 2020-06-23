package com.l2l.contextsharing.connectors.value;

import java.util.List;

public class LogisticsLI {
    double cc;
    int lt;

    public void SetallVar(List<Integer> list) {
        int time = list.get(0);
        this.CC(time);
        this.LT(time);
    }
    public double getCc() {
        return cc;
    }

    public void setCc(double cc) {
        this.cc = cc;
    }

    public int getLt() {
        return lt;
    }

    public void setLt(int lt) {
        this.lt = lt;
    }

    public void CC(int time){
        if(time < 48 ){
            this.setCc(1);
        }else if(time < 72){
            this.setCc(1-(time-48)*0.002);
        }else {
            this.setCc(0.8 - (time-48)*0.004);
        }
    }

    public void LT(int time){
        this.setLt(72-time);
    }
}
