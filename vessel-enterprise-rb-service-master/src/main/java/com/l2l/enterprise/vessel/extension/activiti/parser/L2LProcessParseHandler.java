package com.l2l.enterprise.vessel.extension.activiti.parser;

import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ProcessParseHandler;
import org.activiti.engine.impl.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class L2LProcessParseHandler extends ProcessParseHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(L2LProcessParseHandler.class);

    public L2LProcessParseHandler() {
    }


    protected void executeParse(BpmnParse bpmnParse, Process process) {
        if (!process.isExecutable()) {
            LOGGER.info("Ignoring non-executable process with id='" + process.getId() + "'. Set the attribute isExecutable=\"true\" to deploy this process.");
        } else {
            bpmnParse.getProcessDefinitions().add(this.transformProcess(bpmnParse, process));
        }

    }
}
