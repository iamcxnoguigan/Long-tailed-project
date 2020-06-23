package org.activiti.cloud.starter.rb.extension.parser;

import org.activiti.bpmn.model.Process;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ProcessParseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class L2LProcessParseHandler extends ProcessParseHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(L2LProcessParseHandler.class);

    public L2LProcessParseHandler() {
    }


    protected void executeParse(BpmnParse bpmnParse, Process process) {
        if (!process.isExecutable()) {
            LOGGER.info("Ignoring non-executable processes with id='" + process.getId() + "'. Set the attribute isExecutable=\"true\" to deploy this processes.");
        } else {
            bpmnParse.getProcessDefinitions().add(this.transformProcess(bpmnParse, process));
        }

    }
}
