package com.l2l.enterprise.vessel.eventGateway;


import com.l2l.enterprise.vessel.domain.AcquireProcessBusinessInformationMsg;
import com.l2l.enterprise.vessel.domain.DelayMsg;
import com.l2l.enterprise.vessel.extension.activiti.annotation.MsgAnnotation;
import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component
public class APBIMsgHandler implements MsgEventHandler{
    private final MsgMatch msgMatch;
    private final BinderAwareChannelResolver binderAwareChannelResolver;
    private final VidPidRegistry vidPidRegistry;
    private final L2LProcessEngineConfiguration l2LProcessEngineConfiguration;

    public APBIMsgHandler(MsgMatch msgMatch, BinderAwareChannelResolver binderAwareChannelResolver, VidPidRegistry vidPidRegistry, L2LProcessEngineConfiguration l2LProcessEngineConfiguration) {
        this.msgMatch = msgMatch;
        this.binderAwareChannelResolver = binderAwareChannelResolver;
        this.vidPidRegistry = vidPidRegistry;
        this.l2LProcessEngineConfiguration = l2LProcessEngineConfiguration;
    }
    public void handle(AcquireProcessBusinessInformationMsg acquireProcessBusinessInformationMsg){
        if(acquireProcessBusinessInformationMsg.getScenario().equals("coldchain")){
            scenarioHandlerForColdchain(acquireProcessBusinessInformationMsg);
        }
    }

    public void scenarioHandlerForColdchain(AcquireProcessBusinessInformationMsg acquireProcessBusinessInformationMsg){
        if(acquireProcessBusinessInformationMsg.getTopic().equals("acquireProcessBusinessInformation")) {
            topicHandlerForTH(acquireProcessBusinessInformationMsg);
        }
    }

    @Autowired
    MsgAnnotationUtil msgAnnotationUtil;
    public void topicHandlerForTH(AcquireProcessBusinessInformationMsg acquireProcessBusinessInformationMsg){
//读取流程信息并返回消息()
        List<MsgAnnotation> mmp = msgAnnotationUtil.getMmp(acquireProcessBusinessInformationMsg.getTopic());
//        testProcessRuntime.processInstances(null);
        RuntimeService runtimeService = l2LProcessEngineConfiguration.getRuntimeService();
        for(MsgAnnotation msgAnnotation : mmp){
            List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionId(msgAnnotation.getProcessDefinitionId()).list();
            for(ProcessInstance processInstance :processInstances){
                for(Map.Entry<String, Object> entry : acquireProcessBusinessInformationMsg.getProcessBusinessInformation().entrySet())
                    entry.setValue(runtimeService.getVariable(processInstance.getId(),entry.getKey()));
            }
        }
        Message<AcquireProcessBusinessInformationMsg> apbMessage = MessageBuilder.withPayload(acquireProcessBusinessInformationMsg).setHeader("connectorType", acquireProcessBusinessInformationMsg.getConnectorType()).build();
        binderAwareChannelResolver.resolveDestination(acquireProcessBusinessInformationMsg.getConnectorType()).send(apbMessage);
    }
}


