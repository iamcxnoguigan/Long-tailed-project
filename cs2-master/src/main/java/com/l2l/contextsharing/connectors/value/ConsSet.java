package com.l2l.contextsharing.connectors.value;

public class ConsSet {

    public boolean setMethod(String s, Object object){
        switch (s){
            case "MyMuffinARCons":
                return  this.MyMuffinARCons((MyMuffinAR) object);
            case "CustomerAPCons":
                return this.CustomerAPCons((CustomerAP) object);
            case "LogisticsLICons":
                return this.LogisticsLICons((LogisticsLI) object);
            case "LogisticsLCCons":
                return this.LogisticsLCCons((LogisticsLC) object);
            case "LogisticsARCons":
                return this.LogisticsARCons((LogisticsAR) object);
            case "MyMuffinDRCons":
                return this.MyMuffinDRCons((MyMuffinDR) object);
            case "CustomerCSCons":
                return this.CustomerCSCons((CustomerCS) object);
            case "MyMuffinAPCons":
                return this.MyMuffinAPCons((MyMuffinAP) object);
            default:
                return false;
        }
    }

    public boolean MyMuffinARCons(MyMuffinAR myMuffinAR){
        if(myMuffinAR.getEr() + myMuffinAR.getCost() - myMuffinAR.getRc() - myMuffinAR.getBounes() < 500 &&
            myMuffinAR.getCost() < 300)
        return true;
        else return false;
    }
    public boolean CustomerAPCons(CustomerAP customerAP){
        if(customerAP.getValue() < 480){
            return true;
        }else  return false;
    }
    public boolean LogisticsLICons(LogisticsLI logisticsLI){
        if(logisticsLI.getCc() >= 0.95 && logisticsLI.getLt()>=0){
            return  true;
        }else return false;
    }
    public boolean LogisticsLCCons(LogisticsLC logisticsLC){
        if(logisticsLC.getValue() <= 60){
            return true;
        }else return false;
    }
    public boolean LogisticsARCons(LogisticsAR logisticsAR){
        if(logisticsAR.getEr() >= 80){
            return true;
        }else return false;
    }

    public boolean MyMuffinDRCons(MyMuffinDR myMuffinDR){
        if(myMuffinDR.getDf()+ myMuffinDR.getRc() <=150 && myMuffinDR.getRc()<=50){
            return  true;
        }else return false;
    }
    public boolean CustomerCSCons(CustomerCS customerCS){
        if(customerCS.getValue()>9){
            return  true;
        }else return false;
    }
    public boolean MyMuffinAPCons(MyMuffinAP myMuffinAP){
        if(myMuffinAP.getValue()<=150){
            return  true;
        }else return false;
    }
}
