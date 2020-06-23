package com.l2l.enterprise.vessel.domain;

import java.io.Serializable;
import java.util.*;

public class AcquireProcessBusinessInformationMsg extends MsgImpl implements Serializable {
    private Map<String,Object> processBusinessInformation = new HashMap<>();
    private Map<String,Map<String,Object>> vidTopbi = new HashMap<>();

    public Map<String, Object> getProcessBusinessInformation() {
        return processBusinessInformation;
    }

    public long getCurTime() {
        return curTime;
    }

    private long curTime;
    public void updatePBI(String key,Object value){
        processBusinessInformation.put(key,value);
    }
    public void updatePBI(Map<String,Object> vMap){
        processBusinessInformation.putAll(vMap);
    }

    public void setCurTime() {
        this.curTime = new Date().getTime();
    }

    public AcquireProcessBusinessInformationMsg() {
        super();
        setCurTime();
    }

    public AcquireProcessBusinessInformationMsg(List<String> pids, String connectorType, String topic, String scenario,Map<String,Map<String,Object>> vidTopbi) {
        super(pids, connectorType, topic, scenario);
        this.processBusinessInformation = processBusinessInformation;
        this.vidTopbi = vidTopbi;
        setCurTime();
    }

}
