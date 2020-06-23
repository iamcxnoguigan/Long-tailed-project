package com.l2l.enterprise.vessel.eventGateway;

import com.l2l.enterprise.vessel.domain.DelayMsg;
import com.l2l.enterprise.vessel.domain.Destination;
import com.l2l.enterprise.vessel.extension.activiti.annotation.MsgAnnotation;
import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DelayMsgHandler implements MsgEventHandler{
    private final MsgMatch msgMatch;
    private final BinderAwareChannelResolver binderAwareChannelResolver;
    private final VidPidRegistry vidPidRegistry;
    private final L2LProcessEngineConfiguration l2LProcessEngineConfiguration;

    public DelayMsgHandler(MsgMatch msgMatch, BinderAwareChannelResolver binderAwareChannelResolver, VidPidRegistry vidPidRegistry, L2LProcessEngineConfiguration l2LProcessEngineConfiguration) {
        this.msgMatch = msgMatch;
        this.binderAwareChannelResolver = binderAwareChannelResolver;
        this.vidPidRegistry = vidPidRegistry;
        this.l2LProcessEngineConfiguration = l2LProcessEngineConfiguration;
    }

    public void handle(DelayMsg delayMsg){
//        if(delayMsg.getScenario().equals("ssp")){
            scenarioHandlerForSsp(delayMsg);
//        }
    }

    public void scenarioHandlerForSsp(DelayMsg delayMsg){
//        if(delayMsg.getTopic().equals("delay")) {
            topicHandlerForDelay(delayMsg);
//        }
    }

    public void topicHandlerForDelay(DelayMsg delayMsg){
        if(delayMsg.getStep().equals("1")){
            step1(delayMsg);
        }else {
            step2(delayMsg);
        }
    }
//方便扩充可能需要利用反射并且将执行函数外置
    public void step1(DelayMsg delayMsg){
        Map<String, List<MsgAnnotation>> msgAnnoationMap = new HashMap<>();
        List<MsgAnnotation> mmp = new ArrayList<>();
        msgAnnoationMap = msgMatch.initMsgAnnotationMap();
        for(Map.Entry<String,List<MsgAnnotation>> entry : msgAnnoationMap.entrySet()){
            if(entry.getKey().equals(delayMsg.getTopic())){

//                if(delayMsg.getMsgAnnotationList() == null) delayMsg.setMsgAnnotationList(new ArrayList<MsgAnnotation>());
                mmp.addAll(entry.getValue());
            }
        }//需要根据定义id获取流程实例id
//        testProcessRuntime.processInstances(null);
        RuntimeService runtimeService = l2LProcessEngineConfiguration.getRuntimeService();
        for(MsgAnnotation msgAnnotation : mmp){
            List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionId(msgAnnotation.getProcessDefinitionId()).list();
            if(delayMsg.getDestinationMap() ==null) delayMsg.setDestinationMap(new HashMap<>());
            for(ProcessInstance processInstance :processInstances){
                String vid = vidPidRegistry.findRegisteredVidBypId(processInstance.getId());
//                Destination de = new Destination();
                if(vid != null ){
                    List<LinkedHashMap<String,String>> destinations = (List<LinkedHashMap<String,String> >) runtimeService.getVariable(processInstance.getId(),"destinations");//读取变量需要测试
                    List<Destination> destinationList = new ArrayList<>();
                    for(LinkedHashMap<String,String> de :destinations){
                        destinationList.add(new Destination(de.get("name"),de.get("estiAnchorTime"),de.get("estiArrivalTime"),de.get("estiDepartureTime")));
                    }
                    delayMsg.getDestinationMap().put(vid,destinationList);
                }
            }
        }
//        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionId("id").list();
//        if(delayMsg.getDestinationMap() ==null) delayMsg.setDestinationMap(new HashMap<>());
//        for(ProcessInstance processInstance :processInstances){
//            String vid = vidPidRegistry.findRegisteredVidBypId(processInstance.getId());
//            delayMsg.getDestinationMap().put(vid,null);
//        }
        delayMsg.setConnectorType("delayServiceConfirm");
        org.springframework.messaging.Message<DelayMsg> delayMsgMessage = MessageBuilder.withPayload(delayMsg).setHeader("connectorType", delayMsg.getConnectorType()).build();
        binderAwareChannelResolver.resolveDestination(delayMsg.getConnectorType()).send(delayMsgMessage);//包括topic为delay的所有msgannos
    }

    public void step2(DelayMsg delayMsg){
        for(Map.Entry<String,List<Destination>>  entry : delayMsg.getDestinationMap().entrySet()){
            String vid = entry.getKey();
            String pid = vidPidRegistry.findRegisteredpidByvId(vid);
            //update 流程变量v
            RuntimeService runtimeService = l2LProcessEngineConfiguration.getRuntimeService();
            runtimeService.setVariable(pid,"destinations",entry.getValue());
//            for(Destination destination : entry.getValue()){
//                runtimeService.setVariable(pid,destination.getName() +"estiAnchorTime",destination.getEstiAnchorTime());
//                runtimeService.setVariable(pid,destination.getName() +"estiArrivalTime",destination.getEstiArrivalTime());
//                runtimeService.setVariable(pid,destination.getName() +"estiDepartureTime",destination.getEstiDepartureTime());
//            }//赋值完成 变量名称待变

        }
    }
}
