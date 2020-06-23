package com.l2l.enterprise.vessel.extension.activiti.agenda;

import com.l2l.enterprise.vessel.extension.activiti.annotation.DefaultAnnotationBehavior;
import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import com.l2l.enterprise.vessel.extension.activiti.parser.AnnotationConstants;
import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.impl.agenda.TakeOutgoingSequenceFlowsOperation;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class L2LTakeOutgoingSequenceFlowsOperation extends TakeOutgoingSequenceFlowsOperation {
    private static Logger logger = LoggerFactory.getLogger(L2LContinueProcessOperation.class);
    private IntegrationContextEntity integrationContextEntity;

    public L2LTakeOutgoingSequenceFlowsOperation(CommandContext commandContext, ExecutionEntity executionEntity, boolean evaluateConditions) {
        super(commandContext, executionEntity, evaluateConditions);
    }
    public L2LTakeOutgoingSequenceFlowsOperation(CommandContext commandContext, ExecutionEntity executionEntity, boolean evaluateConditions,IntegrationContextEntity integrationContextEntity){
        super(commandContext, executionEntity, evaluateConditions);
        this.integrationContextEntity = integrationContextEntity;
    }
    public void run() {
        FlowElement currentFlowElement = this.getCurrentFlowElement(this.execution);
//        IntegrationContextEntity integrationContextEntity = this.integrationContextService.findById(integrationResult.getIntegrationContext().getId());
        Annotation tAn = acquirePostAnnotations(currentFlowElement , execution);
        if(tAn != null){
            enterPostAnnotation(tAn);
        }else{
            super.run();
        }
    }

    protected Annotation acquirePostAnnotations(FlowElement flowNode , ExecutionEntity execution){
        String pdId = execution.getProcessDefinitionId();
        List<Annotation> postAns = new ArrayList<Annotation>();
        if(this.commandContext.getProcessEngineConfiguration() instanceof L2LProcessEngineConfiguration){
            postAns = ((L2LProcessEngineConfiguration) this.commandContext.getProcessEngineConfiguration()).getAnnotationManager().getAnnotations()
                .stream().filter( an -> {
                    boolean selected = false;
                    if(integrationContextEntity == null){
                        selected = (
                            AnnotationConstants.POST_PROCESSOR.equals(an.getPointcutType()) &&
                                an.getTargetElementId().equals(flowNode.getId()) &&
                                an.getProcessDefinitionId().equals(execution.getProcessDefinitionId()));
                    }else {
                        selected = (!this.integrationContextEntity.getFlowNodeId().equals(an.getTargetElementId())&&
                            !this.integrationContextEntity.getProcessDefinitionId().equals(an.getProcessDefinitionId())&&
                            !this.integrationContextEntity.getExecutionId().equals((this.execution.getId())) &&
                            AnnotationConstants.POST_PROCESSOR.equals(an.getPointcutType()) &&
                            an.getTargetElementId().equals(flowNode.getId()) &&
                            an.getProcessDefinitionId().equals(execution.getProcessDefinitionId()));

                    }
                    return selected;

                }).collect(Collectors.toList());
        }
        if(postAns.size() > 0){
            // Initially , supporting  only one annotation of specified 'pointcut' type on the FlowElement. Indeed we can support more than one.
            //Here we take the first from the annotation queue on the current flowElement.
            Annotation tAn = postAns.get(0);
            return tAn;
        }else{
            logger.debug("No pre-annotaions is attached to the FlowElement {}" , flowNode.getId());
        }
        return null;
    }

    protected  void enterPostAnnotation(Annotation currentAnnotation){
        if(currentAnnotation.getDestination() != null){
            if(this.commandContext.getProcessEngineConfiguration() instanceof L2LProcessEngineConfiguration){
                ActivityBehavior annotationBehavior = (ActivityBehavior) ((L2LProcessEngineConfiguration) this.commandContext.getProcessEngineConfiguration())
                    .getAnnotationManager().getBehavior();
                if(annotationBehavior instanceof DefaultAnnotationBehavior){
                    ((DefaultAnnotationBehavior) annotationBehavior).execute(this.execution,currentAnnotation);
                }
            }
        }else{
            logger.debug("Now , Only third-party delivery is supported");
        }
    }
}
