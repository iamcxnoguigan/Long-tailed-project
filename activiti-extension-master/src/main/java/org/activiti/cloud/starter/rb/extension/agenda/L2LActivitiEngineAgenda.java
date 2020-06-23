package org.activiti.cloud.starter.rb.extension.agenda;

import org.activiti.engine.impl.agenda.DefaultActivitiEngineAgenda;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

public class L2LActivitiEngineAgenda extends DefaultActivitiEngineAgenda {
    public L2LActivitiEngineAgenda(CommandContext commandContext) {
        super(commandContext);
    }

    public void planContinueProcessOperation(ExecutionEntity execution) {
        this.planOperation(new L2LContinueProcessOperation(this.commandContext, execution));
    }

    public void planContinueProcessSynchronousOperation(ExecutionEntity execution) {
        this.planOperation(new L2LContinueProcessOperation(this.commandContext, execution, true, false));
    }

}
