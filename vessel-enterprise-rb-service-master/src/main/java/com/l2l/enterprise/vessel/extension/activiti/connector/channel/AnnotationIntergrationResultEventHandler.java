package com.l2l.enterprise.vessel.extension.activiti.connector.channel;

import com.l2l.enterprise.vessel.domain.DelayMsg;
import com.l2l.enterprise.vessel.eventGateway.DelayMsgHandler;
import com.l2l.enterprise.vessel.eventGateway.MsgEventHandler;
import com.l2l.enterprise.vessel.eventGateway.channel.DelayMsgChannel;
import com.l2l.enterprise.vessel.extension.activiti.annotation.AnnotationIntegrationResultImpl;
import com.l2l.enterprise.vessel.extension.activiti.annotation.AnnotationIntergrationContextImpl;
import com.l2l.enterprise.vessel.extension.activiti.annotation.AnnotationService;
import org.activiti.cloud.services.events.configuration.RuntimeBundleProperties;
import org.activiti.cloud.services.events.converter.RuntimeBundleInfoAppender;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextEntity;
import org.activiti.engine.integration.IntegrationContextService;
import org.activiti.runtime.api.event.impl.CloudIntegrationResultReceivedImpl;
import org.activiti.runtime.api.model.IntegrationResult;
import org.activiti.services.connectors.channel.ServiceTaskIntegrationResultEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


@Component
@EnableBinding({AnnotationIntegrationChannels.class })
public class AnnotationIntergrationResultEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskIntegrationResultEventHandler.class);
    private final RuntimeService runtimeService;
    private final IntegrationContextService integrationContextService;
    private final MessageChannel auditProducer;
    private final RuntimeBundleProperties runtimeBundleProperties;
    private final RuntimeBundleInfoAppender runtimeBundleInfoAppender;
    private final AnnotationService annotationService;
    private final MsgEventHandler msgEventHandler;
    private static org.slf4j.Logger log = LoggerFactory.getLogger(AnnotationIntergrationResultEventHandler.class);


    public AnnotationIntergrationResultEventHandler(RuntimeService runtimeService, IntegrationContextService integrationContextService, MessageChannel auditProducer, RuntimeBundleProperties runtimeBundleProperties, RuntimeBundleInfoAppender runtimeBundleInfoAppender, AnnotationService annotationService, DelayMsgHandler delayMsgHandler) {
        this.runtimeService = runtimeService;
        this.integrationContextService = integrationContextService;
        this.auditProducer = auditProducer;
        this.runtimeBundleProperties = runtimeBundleProperties;
        this.runtimeBundleInfoAppender = runtimeBundleInfoAppender;
        this.annotationService = annotationService;
        this.msgEventHandler = delayMsgHandler;
    }

    @StreamListener("AnnotationIntegrationResultsConsumer")
    public void AnnoatationResultReceive(AnnotationIntegrationResultImpl integrationResult) {
        IntegrationContextEntity integrationContextEntity = this.integrationContextService.findById(integrationResult.getIntegrationContext().getId());
        AnnotationIntergrationContextImpl annotationIntergrationContext= null;
        if(integrationResult.getAnnotationIntegrationRequest().getAnnotationIntergrationContext() instanceof AnnotationIntergrationContextImpl){
            annotationIntergrationContext = (AnnotationIntergrationContextImpl) integrationResult.getAnnotationIntegrationRequest().getAnnotationIntergrationContext() ;
        }
        if (integrationContextEntity != null) {
            this.integrationContextService.deleteIntegrationContext(integrationContextEntity);
            if(integrationResult.getAnnotationIntegrationRequest().getAnnotationIntergrationContext().getAnnotation().getImplementationType().equals("msgType")){
                if (this.runtimeService.createExecutionQuery().executionId(integrationContextEntity.getExecutionId()).list().size() > 0) {
                    //获取commandcontext 必须要采用cmd模式   这里是否口语添加流程变量的修改代码 感觉可以
                    this.runtimeService.setVariables(integrationContextEntity.getExecutionId(),
                        integrationResult.getAnnotationIntegrationRequest().getAnnotationIntergrationContext().getAnnotation().getExecutionvars());
                } else {
                    String message = "No task is in this RB is waiting for integration result with execution id `" + integrationContextEntity.getExecutionId() + ", flow node id `" + integrationResult.getIntegrationContext().getActivityElementId() + "`. The integration result for the integration context `" + integrationResult.getIntegrationContext().getId() + "` will be ignored.";
                    LOGGER.debug(message);
                }
            }else if(this.runtimeService.createExecutionQuery().executionId(integrationContextEntity.getExecutionId()).list().size() > 0) {
                //获取commandcontext 必须要采用cmd模式   这里是否口语添加流程变量的修改代码 感觉可以
                this.runtimeService.setVariables(integrationContextEntity.getExecutionId(),
                    integrationResult.getAnnotationIntegrationRequest().getAnnotationIntergrationContext().getAnnotation().getExecutionvars());
                this.annotationService.trigger(integrationContextEntity.getExecutionId(), integrationContextEntity,annotationIntergrationContext);
            } else {
                String message = "No task is in this RB is waiting for integration result with execution id `" + integrationContextEntity.getExecutionId() + ", flow node id `" + integrationResult.getIntegrationContext().getActivityElementId() + "`. The integration result for the integration context `" + integrationResult.getIntegrationContext().getId() + "` will be ignored.";
                LOGGER.debug(message);
            }
            log.info(integrationResult.toString());
            this.sendAuditMessage(integrationResult);


        }

    }

    private void sendAuditMessage(IntegrationResult integrationResult) {
        if (this.runtimeBundleProperties.getEventsProperties().isIntegrationAuditEventsEnabled()) {
            CloudIntegrationResultReceivedImpl integrationResultReceived = new CloudIntegrationResultReceivedImpl(integrationResult.getIntegrationContext());
            this.runtimeBundleInfoAppender.appendRuntimeBundleInfoTo(integrationResultReceived);
            Message<CloudIntegrationResultReceivedImpl> message = MessageBuilder.withPayload(integrationResultReceived).build();
            this.auditProducer.send(message);
        }

    }
//    @StreamListener("delayDestinationUpdate")
//    public void delayDestinationUpdate(DelayMsg delayMsg){
//        msgEventHandler.delayDestinationUpdate(delayMsg);
//    }
//    @StreamListener("AnnotationIntegrationResultsConsumer")
//    public void comfirmDelayMsg(DelayMsg delayMsg){
//        msgEventHandler.comfirmDelayMsg(delayMsg);
//    }

//    @StreamListener(DelayMsgChannel.DELAY_MSG)
//    public void comfirmDelayMsg(DelayMsg delayMsg){
//        LOGGER.info("get");
////        msgEventHandler.comfirmDelayMsg(delayMsg);
//    }
}
