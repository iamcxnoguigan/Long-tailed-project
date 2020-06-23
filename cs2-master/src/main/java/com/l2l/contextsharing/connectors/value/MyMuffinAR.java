package com.l2l.contextsharing.connectors.value;

import java.util.List;

public class MyMuffinAR {
    int er;
    int cost;
    int rc;
    int bounes;
    int value;

    public void SetallVar(List<Integer> list) {
        int num = list.get(0);
        int time = list.get(1);
        this.ER(num, time);
        this.Cost(num, time);
        this.RC(time);
        this.Bounes(time);
        this.setValue();
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

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public int getBounes() {
        return bounes;
    }

    public void setBounes(int bounes) {
        this.bounes = bounes;
    }


    public int ER(int num , int time){
        int ans = 400 - 50*num;
        if(time > 36){
            ans = ans -5*(time-36);
        }
        this.er = ans;
        return ans;
    }

    public int Cost(int num,int time){
        int ans = num*100;
        if(time > 36){
            ans = ans + 2*(time-36);
        }
        this.cost = ans;
        return ans;
    }

    public int RC(int time){
        int ans = 0;
        if(time > 36 ){
            ans = ans + 2*(time-36);
        }
        this.rc = ans;
        return ans;

    }

    public int Bounes(int time){
        int ans = 0;
        if(time > 36){
            ans = ans + 2*(time-36);
        }
        this.bounes = ans;
        return ans;
    }

    public void setValue(){
        this.value = this.er - this.rc + this.cost - this.bounes;
    }

    public int getValue(){
        return this.value;
    }


}
