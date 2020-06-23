package com.l2l.contextsharing.connectors.value;

import org.springframework.stereotype.Component;

@Component
public class ValueExchangeSet {//为了简化 在exchange的输出端 直接输出的是int而没有输入端的复杂变量新建过程 后期可以改
    public Object setValueExchange(String s,Object object){
        switch (s){
            case "AR2AP":
                return this.AR2AP((MyMuffinAR) object);
            case "LI2CS":
                return this.LI2CS((LogisticsLI) object);
            case  "ARtoAP":
                return this.ARtoAP((LogisticsAR) object);
            case "DR2LC":
                return this.DR2LC((MyMuffinDR) object);
            default:
                return -1;
        }
    }
    public CustomerAP AR2AP(MyMuffinAR myMuffinAR){
        CustomerAP customerAP = new CustomerAP();
        int ans = myMuffinAR.getValue() - myMuffinAR.getBounes()*3;
        customerAP.setValue(ans);
        return customerAP;
    }

    public CustomerCS LI2CS(LogisticsLI logisticsLI){
        CustomerCS customerCS = new CustomerCS();
        customerCS.setValue(logisticsLI.getCc()*9.35);
        return customerCS;
    }

    public MyMuffinAP ARtoAP(LogisticsAR logisticsAR){
        MyMuffinAP myMuffinAP = new MyMuffinAP();
        myMuffinAP.setValue(logisticsAR.getEr() + logisticsAR.getCost());
        return myMuffinAP;
    }

    public LogisticsLC DR2LC(MyMuffinDR myMuffinDR){
        LogisticsLC logisticsLC = new LogisticsLC();
        logisticsLC.setValue((int) ((myMuffinDR.getDf() + myMuffinDR.getRc())*0.5));
        return logisticsLC;
    }

}
