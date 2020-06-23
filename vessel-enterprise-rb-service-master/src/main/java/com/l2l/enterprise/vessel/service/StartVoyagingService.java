package com.l2l.enterprise.vessel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2l.enterprise.vessel.coordinator.AwsClient;
import com.l2l.enterprise.vessel.repository.ShadowRepository;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.util.Map;


@Service("startVoyagingService")
public class StartVoyagingService implements ExecutionListener, Serializable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final long serialVersionUID = 4149621500319226872L;

    @Autowired
    private AwsClient awsClient;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ShadowRepository shadowRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void notify(DelegateExecution execution) {
        // TODO Auto-generated method stub
        logger.info("--startVoyagingService--");
        String pid = execution.getProcessInstanceId();
        Map<String, Object> vars = execution.getVariables();
        String vid = vars.get("vid").toString();
//        VesselShadow vs = shadowRepository.findById(vid);
//        vs.setStatus("Voyaging");
        runtimeService.setVariable(pid , "processStatus" , "Voyaging");
        //TODO: notify vessel device of start next voyaging
    }

}
