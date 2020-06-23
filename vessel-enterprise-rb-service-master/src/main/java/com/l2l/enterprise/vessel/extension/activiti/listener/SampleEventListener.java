package com.l2l.enterprise.vessel.extension.activiti.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

public class SampleEventListener implements ApplicationListener<SampleEvent> {
    private static Logger log = LoggerFactory.getLogger(SampleEventListener.class);
    public SampleEventListener(){
        log.debug("....");
    }

    @Override
    public void onApplicationEvent(SampleEvent event) {
        //TODO: do ...
    }
}
