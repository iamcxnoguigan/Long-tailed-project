package org.activiti.cloud.starter.rb.extension.agenda;


import org.activiti.bpmn.model.FlowElement;
import org.activiti.cloud.starter.rb.extension.annotation.DefaultAnnotationBehavior;
import org.activiti.cloud.starter.rb.extension.boot.L2LProcessEngineConfiguration;
import org.activiti.cloud.starter.rb.extension.model.Annotation;
import org.activiti.cloud.starter.rb.extension.parser.AnnotationConstants;
import org.activiti.engine.impl.agenda.ContinueProcessOperation;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class L2LContinueProcessOperation extends ContinueProcessOperation {
    private static Logger logger = LoggerFactory.getLogger(L2LContinueProcessOperation.class);
    public L2LContinueProcessOperation(CommandContext commandContext, ExecutionEntity execution, boolean forceSynchronousOperation, boolean inCompensation) {
        super(commandContext, execution, forceSynchronousOperation, inCompensation);
    }

    public L2LContinueProcessOperation(CommandContext commandContext, ExecutionEntity execution) {
        super(commandContext, execution);
    }

    public void run() {
        FlowElement currentFlowElement = this.getCurrentFlowElement(this.execution);
        Annotation tAn = acquirePreAnnotations(currentFlowElement , execution);
        if(tAn != null){
            enterPreAnnotation(tAn);
        }else{
            super.run();
        }
    }


    protected  Annotation acquirePreAnnotations(FlowElement flowNode , ExecutionEntity execution){
        String pdId = execution.getProcessDefinitionId();
        List<Annotation> preAns = new ArrayList<Annotation>();
        if(this.commandContext.getProcessEngineConfiguration() instanceof L2LProcessEngineConfiguration){
            preAns = ((L2LProcessEngineConfiguration) this.commandContext.getProcessEngineConfiguration()).getAnnotationManager().getAnnotations()
                .stream().filter( an -> {
                    boolean selected = false;
                    selected = (AnnotationConstants.PRE_PROCESSOR.equals(an.getPointcutType()) && an.getTargetElementId().equals(flowNode.getId()) && an.getProcessDefinitionId().equals(execution.getProcessDefinitionId()));
                    return selected;

                }).collect(Collectors.toList());
        }
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
                    ((DefaultAnnotationBehavior) annotationBehavior).setAnnotation(currentAnnotation);
                }
                annotationBehavior.execute(this.execution);
            }
        }else{
            logger.debug("Now , Only third-party delivery is supported");
        }
    }

}
