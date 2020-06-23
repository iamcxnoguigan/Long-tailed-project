package com.l2l.enterprise.vessel.eventGateway;

import com.l2l.enterprise.vessel.domain.Destination;
import com.l2l.enterprise.vessel.domain.THMsg;
import com.l2l.enterprise.vessel.extension.activiti.annotation.MsgAnnotation;
import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import com.sun.javafx.collections.MappingChange;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class THMsgHandler implements MsgEventHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(THMsgHandler.class);
    private final MsgMatch msgMatch;
    private final BinderAwareChannelResolver binderAwareChannelResolver;
    private final VidPidRegistry vidPidRegistry;
    private final L2LProcessEngineConfiguration l2LProcessEngineConfiguration;

    public THMsgHandler(MsgMatch msgMatch, BinderAwareChannelResolver binderAwareChannelResolver, VidPidRegistry vidPidRegistry, L2LProcessEngineConfiguration l2LProcessEngineConfiguration) {
        this.msgMatch = msgMatch;
        this.binderAwareChannelResolver = binderAwareChannelResolver;
        this.vidPidRegistry = vidPidRegistry;
        this.l2LProcessEngineConfiguration = l2LProcessEngineConfiguration;
    }

    public void handle(THMsg thMsg){
        if(thMsg.getScenario().equals("coldchain")){
            scenarioHandlerForColdchain(thMsg);
        }//可能会有别的场景
    }

    public void scenarioHandlerForColdchain(THMsg thMsg){
        if(thMsg.getTopic().equals("temperatureAndHumidity")) {
            topicHandlerForTH(thMsg);
        }
    }
    @Autowired
    MsgAnnotationUtil msgAnnotationUtil;
    public void topicHandlerForTH(THMsg thMsg){
        List<MsgAnnotation> mmp = msgAnnotationUtil.getMmp(thMsg.getTopic());
//        testProcessRuntime.processInstances(null);
        RuntimeService runtimeService = l2LProcessEngineConfiguration.getRuntimeService();
        for(MsgAnnotation msgAnnotation : mmp){
            List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionId(msgAnnotation.getProcessDefinitionId()).list();
            for(ProcessInstance processInstance :processInstances){
                String vid = vidPidRegistry.findRegisteredVidBypId(processInstance.getId());
                if(vid != null ){
                    for(Map.Entry<String, THMsg.TandH> entry : thMsg.getVidToTandH().entrySet()){
                        if(entry.getKey().equals(vid)){
                            float temperature = entry.getValue().getTemperature();
                            float humidity = entry.getValue().getHumidity();
                            float ptemperature = (float) runtimeService.getVariable(processInstance.getId(),"temperature");
                            float phumidity = (float) runtimeService.getVariable(processInstance.getId(),"humidity");
                            if(((temperature/ptemperature) < 0.5 || (temperature/ptemperature) > 2.0 ) && ((humidity/phumidity) <0.5 || (humidity/phumidity) > 2.0)){
                                LOGGER.info(vid + "bad temperature and humidity");
                            }
                        }
                    }
                }
            }

        }

    }
}
