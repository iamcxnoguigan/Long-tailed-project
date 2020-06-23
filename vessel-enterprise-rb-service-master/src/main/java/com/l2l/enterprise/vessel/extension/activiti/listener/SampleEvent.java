package com.l2l.enterprise.vessel.extension.activiti.listener;

import org.springframework.context.ApplicationEvent;

public class SampleEvent extends ApplicationEvent {
    private String name;

    public SampleEvent(String name, Object source) {
        super(source);
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
