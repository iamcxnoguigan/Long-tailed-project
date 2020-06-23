package org.activiti.cloud.starter.rb.extension.listener;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.springframework.context.ApplicationEventPublisher;


public class InternalSampleEventListener implements ActivitiEventListener {
    private final ApplicationEventPublisher eventPublisher;

    public InternalSampleEventListener(ApplicationEventPublisher eventPublisher){
        this.eventPublisher = eventPublisher;
    }
    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        //collect all annotations on ServiceTask
        Object persistedObject = ((ActivitiEntityEvent)activitiEvent).getEntity();
        //TODO: do ...
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
