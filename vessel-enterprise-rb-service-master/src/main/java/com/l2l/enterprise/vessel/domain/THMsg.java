package com.l2l.enterprise.vessel.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class THMsg extends MsgImpl implements Serializable {
    private Map<String,TandH> vidToTandH = new HashMap<>();//vid 对应湿度和温度
//    @Override
//    public String getConnectorType() {
//        return null;
//    }
public class TandH {
    private float temperature;
    private float humidity;

    public TandH(float temperature, float humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }
}
    public THMsg() {
        super();
    }

    public THMsg(List<String> pids, String connectorType, String topic, String scenario,Map<String,TandH> vidToTandH) {
        super(pids, connectorType, topic, scenario);
        this.vidToTandH = vidToTandH;
    }

    public Map<String, TandH> getVidToTandH() {
        return vidToTandH;
    }

    public void setVidToTandH(Map<String, TandH> vidToTandH) {
        this.vidToTandH = vidToTandH;
    }
}
