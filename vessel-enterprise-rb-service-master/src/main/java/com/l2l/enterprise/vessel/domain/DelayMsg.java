package com.l2l.enterprise.vessel.domain;



import com.l2l.enterprise.vessel.extension.activiti.annotation.MsgAnnotation;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DelayMsg extends MsgImpl implements Serializable {
    private String delayx;
    private String delayy;
    private Map<String,List<Destination>> destinationMap;
    private Integer step;

    public DelayMsg() {
        super();
    }

    public DelayMsg(List<String> pids, String connectorType, String topic, String scenario, String delayx, String delayy, Map<String, List<Destination>> destinationMap) {
        super(pids, connectorType, topic, scenario);
        this.delayx = delayx;
        this.delayy = delayy;
        this.destinationMap = destinationMap;
    }

    public Map<String, List<Destination>> getDestinationMap() {
        return destinationMap;
    }

    public void setDestinationMap(Map<String, List<Destination>> destinationMap) {
        this.destinationMap = destinationMap;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getDelayx() {
        return delayx;
    }

    public void setDelayx(String delayx) {
        this.delayx = delayx;
    }

    public String getDelayy() {
        return delayy;
    }

    public void setDelayy(String delayy) {
        this.delayy = delayy;
    }
}
