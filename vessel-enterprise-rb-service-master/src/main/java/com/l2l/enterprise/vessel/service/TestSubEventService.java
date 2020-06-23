package com.l2l.enterprise.vessel.service;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service("testSubEventService")
public class TestSubEventService implements JavaDelegate, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 298971968212119081L;
    private final Logger logger = LoggerFactory.getLogger(TestSubEventService.class);
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;


    @Autowired
    private ManagementService managementService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        logger.debug("Received  one message MsgTest");

    }
}
