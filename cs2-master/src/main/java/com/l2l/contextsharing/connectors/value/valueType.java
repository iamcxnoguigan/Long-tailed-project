package com.l2l.contextsharing.connectors.value;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class valueType {

    CustomerAP customerAP;
    CustomerCS customerCS;
    MyMuffinAP myMuffinAP;
    MyMuffinAR myMuffinAR;

    public void setVar(String s,Object object){
        switch (s){
            case "CustomerAP":
                this.setCustomerAP((CustomerAP) object);
                break;
            case "CustomerCS":
                this.setCustomerCS((CustomerCS) object);
                break;
            case  "MyMuffinAP":
                this.setMyMuffinAP((MyMuffinAP) object);
                break;
            case  "MyMuffinAR":
                this.setMyMuffinAR((MyMuffinAR) object);
                break;
        }
    }

    public Object getVar(String s){
        switch (s){
            case "CustomerAP":
                return this.getCustomerAP();
            case "CustomerCS":
                return this.getCustomerCS();
            case  "MyMuffinAP":
                return this.getMyMuffinAP();
            case  "MyMuffinAR":
                return this.getMyMuffinAR();
            default:
                return null;
        }
    }

    public CustomerAP getCustomerAP() {
        return customerAP;
    }

    public void setCustomerAP(CustomerAP customerAP) {
        this.customerAP = customerAP;
    }

    public CustomerCS getCustomerCS() {
        return customerCS;
    }

    public void setCustomerCS(CustomerCS customerCS) {
        this.customerCS = customerCS;
    }

    public MyMuffinAP getMyMuffinAP() {
        return myMuffinAP;
    }

    public void setMyMuffinAP(MyMuffinAP myMuffinAP) {
        this.myMuffinAP = myMuffinAP;
    }

    public MyMuffinAR getMyMuffinAR() {
        return myMuffinAR;
    }

    public void setMyMuffinAR(MyMuffinAR myMuffinAR) {
        this.myMuffinAR = myMuffinAR;
    }

    HashMap<String,Integer> value = new HashMap<String,Integer>();

    public valueType() {
        this.value.put("BI",100);
    }



    public HashMap<String, Integer> getValue() {
        return value;
    }

    public void setValue(HashMap<String, Integer> value) {
        this.value = value;
    }
}
