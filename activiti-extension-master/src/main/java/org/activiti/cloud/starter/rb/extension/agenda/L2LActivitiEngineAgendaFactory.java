package org.activiti.cloud.starter.rb.extension.agenda;

import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.ActivitiEngineAgendaFactory;
import org.activiti.engine.impl.interceptor.CommandContext;

public class L2LActivitiEngineAgendaFactory implements ActivitiEngineAgendaFactory {
    public L2LActivitiEngineAgendaFactory() {

    }
    public ActivitiEngineAgenda createAgenda(CommandContext commandContext) {
        return new L2LActivitiEngineAgenda(commandContext);
    }
}
