package org.activiti.cloud.starter.rb.extension.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

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
