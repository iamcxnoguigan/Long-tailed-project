package com.l2l.enterprise.vessel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2l.enterprise.vessel.coordinator.AwsClient;
import com.l2l.enterprise.vessel.domain.VesselShadow;
import com.l2l.enterprise.vessel.repository.CommonRepository;
import com.l2l.enterprise.vessel.repository.ShadowRepository;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Service("initVesselProcessService")
public class InitVesselProcessService implements ExecutionListener, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 298971968212119081L;
    private final Logger logger = LoggerFactory.getLogger(InitVesselProcessService.class);


    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private ShadowRepository shadowRepository;
    @Autowired
    private AwsClient awsClient;
    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Override
    public void notify(DelegateExecution execution) {
        // TODO Auto-generated method stub
        logger.info("--InitVesselProcessService--");
        //bind vid to vessel shadow
        Map<String, Object> vars = execution.getVariables();
        String pid = execution.getProcessInstanceId();
        String vid = vars.get("vid").toString();
        VesselShadow vesselShadow = shadowRepository.findById(vid);
        shadowRepository.saveRegistry(vid , pid);
        //TODO send init message to vessel device
        awsClient.sendInitiation("TRACK" , vid ,commonRepository.getDefaultDelayHour() , commonRepository.getZoomInVal());
        Map<String, Object> addiVars = new HashMap<String, Object>();
//        addiVars.put("status" , "Voyaging");
        addiVars.put("applyId" , "NONE");
        addiVars.put("nextNav" , true);
        addiVars.put("processStatus" , "Voyaging");
        runtimeService.setVariables(pid, addiVars);

    }

}
