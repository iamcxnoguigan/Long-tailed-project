package com.l2l.enterprise.vessel.domain;

import java.util.List;

public class MsgImpl implements Msg {
    private List<String> pids;
    private String connectorType;
    private String topic;
    private String scenario;

    public MsgImpl(){}

    public MsgImpl(List<String> pids, String connectorType, String topic, String scenario) {
        this.pids = pids;
        this.connectorType = connectorType;
        this.topic = topic;
        this.scenario = scenario;
    }

    public List<String> getPids() {
        return pids;
    }

    public void setPids(List<String> pids) {
        this.pids = pids;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    @Override
    public String getConnectorType() {
        return null;
    }
}
