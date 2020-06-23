package com.l2l.enterprise.vessel.eventGateway;

import com.l2l.enterprise.vessel.domain.StopOrderMsg;
import com.l2l.enterprise.vessel.domain.THMsg;
import com.l2l.enterprise.vessel.extension.activiti.annotation.MsgAnnotation;
import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;

import java.util.List;
import java.util.Map;

public class StopOrderMsgHandler implements MsgEventHandler{
    private final MsgMatch msgMatch;
    private final BinderAwareChannelResolver binderAwareChannelResolver;
    private final VidPidRegistry vidPidRegistry;
    private final L2LProcessEngineConfiguration l2LProcessEngineConfiguration;

    public StopOrderMsgHandler(MsgMatch msgMatch, BinderAwareChannelResolver binderAwareChannelResolver, VidPidRegistry vidPidRegistry, L2LProcessEngineConfiguration l2LProcessEngineConfiguration) {
        this.msgMatch = msgMatch;
        this.binderAwareChannelResolver = binderAwareChannelResolver;
        this.vidPidRegistry = vidPidRegistry;
        this.l2LProcessEngineConfiguration = l2LProcessEngineConfiguration;
    }
    public void handle(StopOrderMsg stopOrderMsg){
        stop(stopOrderMsg);
    }
    @Autowired
    MsgAnnotationUtil msgAnnotationUtil;
    public void stop(StopOrderMsg stopOrderMsg){
        List<MsgAnnotation> mmp = msgAnnotationUtil.getMmp(stopOrderMsg.getTopic());
//        testProcessRuntime.processInstances(null);
        RuntimeService runtimeService = l2LProcessEngineConfiguration.getRuntimeService();
        for(MsgAnnotation msgAnnotation : mmp){
            List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionId(msgAnnotation.getProcessDefinitionId()).list();
            for(ProcessInstance processInstance :processInstances){
                String vid = vidPidRegistry.findRegisteredVidBypId(processInstance.getId());
                if(vid != null ){
                    for(String stopVid : stopOrderMsg.getVids()){
                        if(stopVid.equals(vid)){
                           runtimeService.suspendProcessInstanceById(processInstance.getId());
                        }
                    }
                }
            }
        }
    }
}
