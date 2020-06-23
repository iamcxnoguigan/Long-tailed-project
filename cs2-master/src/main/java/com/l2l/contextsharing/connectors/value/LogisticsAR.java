package com.l2l.contextsharing.connectors.value;

import java.util.List;

public class LogisticsAR {
    int er;
    int cost;

    public LogisticsAR() {
    }

    public void SetallVar(List<Integer> list) {
        int loc = list.get(0);
        int time = list.get(1);
        this.ER(loc, time);
        this.Cost(loc,time);
    }

    public int getEr() {
        return er;
    }

    public void setEr(int er) {
        this.er = er;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void ER(int loc , int time){
        this.setEr(100 + loc*10 + (time-48)*2);
    }
    public void Cost(int loc,int time){
        this.setCost((int) (10*loc+time*0.3));
    }
}
