package com.l2l.enterprise.vessel.extension.activiti.annotation;

import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;

public class MsgAnnotation extends Annotation {
    public String topic;
    public String scenario;

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
}
