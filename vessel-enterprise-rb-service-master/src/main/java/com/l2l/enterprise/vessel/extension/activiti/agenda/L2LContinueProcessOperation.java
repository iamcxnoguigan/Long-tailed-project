package com.l2l.enterprise.vessel.extension.activiti.agenda;

import com.l2l.enterprise.vessel.extension.activiti.annotation.DefaultAnnotationBehavior;
import com.l2l.enterprise.vessel.extension.activiti.annotation.MsgAnnotation;
import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import com.l2l.enterprise.vessel.extension.activiti.parser.AnnotationConstants;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.engine.impl.agenda.ContinueProcessOperation;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextEntity;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextEntityImpl;
import org.activiti.engine.integration.IntegrationContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class L2LContinueProcessOperation extends ContinueProcessOperation {
    private static Logger logger = LoggerFactory.getLogger(L2LContinueProcessOperation.class);
    private IntegrationContextEntity integrationContextEntity;

    public L2LContinueProcessOperation(CommandContext commandContext, ExecutionEntity execution, boolean forceSynchronousOperation, boolean inCompensation) {
        super(commandContext, execution, forceSynchronousOperation, inCompensation);
    }

    public L2LContinueProcessOperation(CommandContext commandContext, ExecutionEntity execution, IntegrationContextEntity integrationContextEntity) {
        super(commandContext, execution);
        this.integrationContextEntity = integrationContextEntity;
    }
    public L2LContinueProcessOperation(CommandContext commandContext, ExecutionEntity execution) {
        super(commandContext, execution);

    }


    public void run() {
        FlowElement currentFlowElement = this.getCurrentFlowElement(this.execution);
        if(currentFlowElement instanceof StartEvent){
            Annotation mAn = acquireMsgAnnotations(currentFlowElement,execution);
            if(mAn != null){
                enterPreAnnotation(mAn);//目前一个任务上只能附着一个annotation
            }

        }
//        IntegrationContextEntity integrationContextEntity = this.integrationContextService.findById(integrationResult.getIntegrationContext().getId());
        Annotation tAn = acquirePreAnnotations(currentFlowElement , execution);
        if(tAn != null){
            enterPreAnnotation(tAn);//目前一个任务上只能附着一个annotation
        }else{
            super.run();
        }
    }
    protected  Annotation acquireMsgAnnotations(FlowElement flowNode , ExecutionEntity execution){
        String pdId = execution.getProcessDefinitionId();
        List<MsgAnnotation> msgAns = new ArrayList<MsgAnnotation>();
        List<Annotation> usedAns = new ArrayList<Annotation>();
        if(this.commandContext.getProcessEngineConfiguration() instanceof  L2LProcessEngineConfiguration){
            msgAns = ((L2LProcessEngineConfiguration) this.commandContext.getProcessEngineConfiguration()).getAnnotationManager().getMsgAnnotations()
                .stream().filter( an -> {
                    boolean selected = false;
                    if(integrationContextEntity == null){
                        selected = (an.getImplementationType().equals("msgType")&&
                                an.getProcessDefinitionId().equals(execution.getProcessDefinitionId()));
                    }else {//为重复an做准备
                        selected = (!this.integrationContextEntity.getFlowNodeId().equals(an.getTargetElementId())&&
                            !this.integrationContextEntity.getProcessDefinitionId().equals(an.getProcessDefinitionId())&&
                            !this.integrationContextEntity.getExecutionId().equals((this.execution.getId())) &&
                            an.getImplementationType().equals("msgType")&&
                            an.getTargetElementId().equals(flowNode.getId()) &&
                            an.getProcessDefinitionId().equals(execution.getProcessDefinitionId()));

                    }
                    return selected;

                }).collect(Collectors.toList());
        }
        if(msgAns.size() > 0){
            // Initially , supporting  only one annotation of specified 'pointcut' type on the FlowElement. Indeed we can support more than one.
            //Here we take the first from the annotation queue on the current flowElement.
            Annotation tAn = (Annotation) msgAns.get(0);
            return tAn;
        }else{
            logger.debug("No pre-annotaions is attached to the FlowElement {}" , flowNode.getId());
        }
        return null;
    }


    protected  Annotation acquirePreAnnotations(FlowElement flowNode , ExecutionEntity execution){
        String pdId = execution.getProcessDefinitionId();
        List<Annotation> preAns = new ArrayList<Annotation>();
        List<Annotation> usedAns = new ArrayList<Annotation>();
        if(this.commandContext.getProcessEngineConfiguration() instanceof  L2LProcessEngineConfiguration){
            preAns = ((L2LProcessEngineConfiguration) this.commandContext.getProcessEngineConfiguration()).getAnnotationManager().getAnnotations()
                .stream().filter( an -> {
                    boolean selected = false;
                    if(integrationContextEntity == null){
                        selected = (
                            AnnotationConstants.PRE_PROCESSOR.equals(an.getPointcutType()) &&
                            an.getTargetElementId().equals(flowNode.getId()) &&
                            an.getProcessDefinitionId().equals(execution.getProcessDefinitionId()));
                    }else {
                        selected = (!this.integrationContextEntity.getFlowNodeId().equals(an.getTargetElementId())&&
                            !this.integrationContextEntity.getProcessDefinitionId().equals(an.getProcessDefinitionId())&&
                            !this.integrationContextEntity.getExecutionId().equals((this.execution.getId())) &&
                            AnnotationConstants.PRE_PROCESSOR.equals(an.getPointcutType()) &&
                            an.getTargetElementId().equals(flowNode.getId()) &&
                            an.getProcessDefinitionId().equals(execution.getProcessDefinitionId()));

                    }
                    return selected;

                }).collect(Collectors.toList());
        }
        logger.info(preAns.toString());
        if(preAns.size() > 0){
            // Initially , supporting  only one annotation of specified 'pointcut' type on the FlowElement. Indeed we can support more than one.
            //Here we take the first from the annotation queue on the current flowElement.
            Annotation tAn = preAns.get(0);
            return tAn;
        }else{
            logger.debug("No pre-annotaions is attached to the FlowElement {}" , flowNode.getId());
        }
        return null;
    }

    protected  void enterPreAnnotation(Annotation currentAnnotation){
        if(currentAnnotation.getDestination() != null){
            if(this.commandContext.getProcessEngineConfiguration() instanceof L2LProcessEngineConfiguration){
                ActivityBehavior annotationBehavior = (ActivityBehavior) ((L2LProcessEngineConfiguration) this.commandContext.getProcessEngineConfiguration())
                    .getAnnotationManager().getBehavior();
                if(annotationBehavior instanceof DefaultAnnotationBehavior){
                    ((DefaultAnnotationBehavior) annotationBehavior).execute(this.execution,currentAnnotation);
                }
            }
        }else{
            logger.debug("Now , Only third-party delivery is supported 123");
        }
    }

}
