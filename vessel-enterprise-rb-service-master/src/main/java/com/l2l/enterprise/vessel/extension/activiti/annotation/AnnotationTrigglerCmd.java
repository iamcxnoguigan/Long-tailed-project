package com.l2l.enterprise.vessel.extension.activiti.annotation;

import com.l2l.enterprise.vessel.extension.activiti.agenda.L2LActivitiEngineAgenda;
import com.l2l.enterprise.vessel.extension.activiti.parser.AnnotationConstants;
import org.activiti.engine.compatibility.Activiti5CompatibilityHandler;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventBuilder;
import org.activiti.engine.impl.cmd.NeedsActiveExecutionCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextEntity;
import org.activiti.engine.impl.util.Activiti5Util;

import java.util.Map;


public class AnnotationTrigglerCmd extends NeedsActiveExecutionCmd<Object> {
    private static final long serialVersionUID = 1L;
    protected Map<String, Object> processVariables;
    protected Map<String, Object> transientVariables;
    protected IntegrationContextEntity integrationContextEntity;
    protected AnnotationIntergrationContextImpl annotationIntergrationContext;

    public AnnotationTrigglerCmd(String executionId, IntegrationContextEntity integrationContextEntity, AnnotationIntergrationContextImpl annotationIntergrationContex,Map<String, Object> processVariables) {
        super(executionId);
        this.processVariables = processVariables;
        this.integrationContextEntity = integrationContextEntity;
        this.annotationIntergrationContext = annotationIntergrationContex;
    }

    public AnnotationTrigglerCmd(String executionId, IntegrationContextEntity integrationContextEntity,AnnotationIntergrationContextImpl annotationIntergrationContext,Map<String, Object> processVariables,Map<String, Object> transientVariables) {
        this(executionId,integrationContextEntity,annotationIntergrationContext, processVariables);
        this.transientVariables = transientVariables;

    }

    protected Object execute(CommandContext commandContext, ExecutionEntity execution) {

            if (this.processVariables != null) {
                execution.setVariables(this.processVariables);
            }

            if (this.transientVariables != null) {
                execution.setTransientVariables(this.transientVariables);
            }//流程变量set以后会用到
        if (annotationIntergrationContext.getAnnotation().getPointcutType().equals(AnnotationConstants.PRE_PROCESSOR)) {
            Context.getProcessEngineConfiguration().getEventDispatcher().dispatchEvent(ActivitiEventBuilder.createSignalEvent(ActivitiEventType.ACTIVITY_SIGNALED, execution.getCurrentActivityId(), (String) null, (Object) null, execution.getId(), execution.getProcessInstanceId(), execution.getProcessDefinitionId()));
            //待定
            ((L2LActivitiEngineAgenda) Context.getAgenda()).planintegrationContextEntityContinueProcessOperation(execution, integrationContextEntity);
        } else if(annotationIntergrationContext.getAnnotation().getPointcutType().equals(AnnotationConstants.POST_PROCESSOR)){
            Context.getProcessEngineConfiguration().getEventDispatcher().dispatchEvent(ActivitiEventBuilder.createSignalEvent(ActivitiEventType.ACTIVITY_SIGNALED, execution.getCurrentActivityId(), (String) null, (Object) null, execution.getId(), execution.getProcessInstanceId(), execution.getProcessDefinitionId()));
            //待定
            ((L2LActivitiEngineAgenda) Context.getAgenda()).planintegrationContextEntityTakeOutgoingSequenceFlowsOperation(execution,true, integrationContextEntity);
        }
            return null;

    }

    protected String getSuspendedExceptionMessage() {
        return "Cannot trigger an execution that is suspended";
    }
}
