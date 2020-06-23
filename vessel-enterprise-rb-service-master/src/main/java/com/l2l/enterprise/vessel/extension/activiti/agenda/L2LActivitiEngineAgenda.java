package com.l2l.enterprise.vessel.extension.activiti.agenda;

import org.activiti.engine.impl.agenda.DefaultActivitiEngineAgenda;
import org.activiti.engine.impl.agenda.TakeOutgoingSequenceFlowsOperation;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextEntity;

public class L2LActivitiEngineAgenda extends DefaultActivitiEngineAgenda {
    public L2LActivitiEngineAgenda(CommandContext commandContext) {
        super(commandContext);
    }
    public void planintegrationContextEntityContinueProcessOperation(ExecutionEntity execution, IntegrationContextEntity integrationContextEntity){
        this.planOperation(new L2LContinueProcessOperation(this.commandContext, execution,integrationContextEntity));
    }

    public void planContinueProcessOperation(ExecutionEntity execution) {
        this.planOperation(new L2LContinueProcessOperation(this.commandContext, execution));
    }

    public void planContinueProcessSynchronousOperation(ExecutionEntity execution) {
        this.planOperation(new L2LContinueProcessOperation(this.commandContext, execution, true, false));
    }

    public void planTakeOutgoingSequenceFlowsOperation(ExecutionEntity execution, boolean evaluateConditions) {
        this.planOperation(new L2LTakeOutgoingSequenceFlowsOperation(this.commandContext, execution, evaluateConditions));
    }

    public void planintegrationContextEntityTakeOutgoingSequenceFlowsOperation(ExecutionEntity execution,boolean evaluateConditions,IntegrationContextEntity integrationContextEntity) {
        this.planOperation(new L2LTakeOutgoingSequenceFlowsOperation(this.commandContext, execution, evaluateConditions,integrationContextEntity));
    }
}
