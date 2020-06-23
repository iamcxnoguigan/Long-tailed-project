package org.activiti.cloud.starter.rb.extension.behavior;

import org.activiti.bpmn.model.*;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.bpmn.behavior.BoundaryTimerEventActivityBehavior;
import org.activiti.engine.impl.cmd.CompleteTaskCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class L2LBoundaryTimerEventActivityBehavior extends BoundaryTimerEventActivityBehavior {
    private static final Logger logger = LoggerFactory.getLogger(L2LBoundaryTimerEventActivityBehavior.class);

    public L2LBoundaryTimerEventActivityBehavior(TimerEventDefinition timerEventDefinition, boolean interrupting) {
        super(timerEventDefinition, interrupting);
    }
    protected void executeInterruptingBehavior(ExecutionEntity executionEntity, CommandContext commandContext) {
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
        ExecutionEntity attachedRefScopeExecution = (ExecutionEntity)executionEntityManager.findById(executionEntity.getParentId());
        ExecutionEntity parentScopeExecution = null;
        ExecutionEntity currentlyExaminedExecution = (ExecutionEntity)executionEntityManager.findById(attachedRefScopeExecution.getParentId());

        while(currentlyExaminedExecution != null && parentScopeExecution == null) {
            if (currentlyExaminedExecution.isScope()) {
                parentScopeExecution = currentlyExaminedExecution;
            } else {
                currentlyExaminedExecution = (ExecutionEntity)executionEntityManager.findById(currentlyExaminedExecution.getParentId());
            }
        }

        if (parentScopeExecution == null) {
            throw new ActivitiException("Programmatic error: no parent scope execution found for boundary event");
        } else {
            this.deleteChildExecutions(attachedRefScopeExecution, executionEntity, commandContext);
            executionEntity.setParent(parentScopeExecution);
            Context.getAgenda().planTakeOutgoingSequenceFlowsOperation(executionEntity, true);
        }
    }

    protected void executeNonInterruptingBehavior(ExecutionEntity executionEntity, CommandContext commandContext) {
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
        ExecutionEntity parentExecutionEntity = (ExecutionEntity)executionEntityManager.findById(executionEntity.getParentId());
        ExecutionEntity scopeExecution = null;
        ExecutionEntity currentlyExaminedExecution = (ExecutionEntity)executionEntityManager.findById(parentExecutionEntity.getParentId());

        while(currentlyExaminedExecution != null && scopeExecution == null) {
            if (currentlyExaminedExecution.isScope()) {
                scopeExecution = currentlyExaminedExecution;
            } else {
                currentlyExaminedExecution = (ExecutionEntity)executionEntityManager.findById(currentlyExaminedExecution.getParentId());
            }
        }

        if (scopeExecution == null) {
            throw new ActivitiException("Programmatic error: no parent scope execution found for boundary event");
        } else {
            FlowElement currentFlowElement = executionEntity.getCurrentFlowElement();
            if(hasOutgoingFlows(currentFlowElement)){
                ExecutionEntity nonInterruptingExecution = executionEntityManager.createChildExecution(scopeExecution);
                nonInterruptingExecution.setCurrentFlowElement(currentFlowElement);
                Context.getAgenda().planTakeOutgoingSequenceFlowsOperation(nonInterruptingExecution, true);
            } else {
                // When the timed event arrives, comlete the task at the same time.
                List<TaskEntity> taskEntities = parentExecutionEntity.getTasks();
                String taskId = taskEntities.get(0).getId();
                Context.getProcessEngineConfiguration().getCommandExecutor().execute(new CompleteTaskCmd(taskId, null));

            }

        }
    }

    /**
     * evaluate the whether the boundary event has outgoing flows.
     * @param flowElement
     * @return
     */
    public boolean hasOutgoingFlows(FlowElement flowElement){
        List<SequenceFlow> outgoingSequenceFlows = new ArrayList();
        FlowNode flowNode = (FlowNode) flowElement;
        if(!(flowNode instanceof BoundaryEvent)){
            logger.debug("current node is required BoundaryEvent");
            return false;
        }
        outgoingSequenceFlows = flowNode.getOutgoingFlows();
        return outgoingSequenceFlows.size() > 0;
    }

}
