package com.l2l.contextsharing.connectors.value;

import org.springframework.stereotype.Component;

@Component
public class processVar {
    int loc;
    int time;
    int num;

    public processVar() {
    }

    public int getVar(String varName){
        switch(varName){
            case "loc" :
                return getLoc();

            case "time" :
                return getTime();

            case "num" :
                return getNum();

            default: return 0;
        }
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
