package com.l2l.enterprise.vessel.service;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

@Service("anchoringPreService")
public class AnchoringPreService implements ExecutionListener, Serializable {
    private static final Logger logger = LoggerFactory.getLogger(AnchoringPreService.class);

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        logger.info("--AnchoringPreService--");
        String pid = delegateExecution.getProcessInstanceId();
        Map<String, Object> vars = delegateExecution.getVariables();
        String vid = vars.get("vid").toString();
//        VesselShadow vs = shadowRepository.findById(vid);
//        vs.setStatus("Voyaging");
        runtimeService.setVariable(pid , "processStatus" , "AnchoringOrDocking");
    }
}
