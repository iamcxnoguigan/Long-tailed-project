package org.activiti.cloud.starter.rb.extension.parser;

import org.activiti.bpmn.model.BoundaryEvent;
import org.activiti.bpmn.model.IntermediateCatchEvent;
import org.activiti.bpmn.model.TimerEventDefinition;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.TimerEventDefinitionParseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class L2LTimerDefinitionParseHandler extends TimerEventDefinitionParseHandler {
    private static final Logger logger = LoggerFactory.getLogger(L2LTimerDefinitionParseHandler.class);

    protected void executeParse(BpmnParse bpmnParse, TimerEventDefinition timerEventDefinition) {
        if (bpmnParse.getCurrentFlowElement() instanceof IntermediateCatchEvent) {
            IntermediateCatchEvent intermediateCatchEvent = (IntermediateCatchEvent)bpmnParse.getCurrentFlowElement();
            intermediateCatchEvent.setBehavior(bpmnParse.getActivityBehaviorFactory().createIntermediateCatchTimerEventActivityBehavior(intermediateCatchEvent, timerEventDefinition));
        } else if (bpmnParse.getCurrentFlowElement() instanceof BoundaryEvent) {
            BoundaryEvent boundaryEvent = (BoundaryEvent)bpmnParse.getCurrentFlowElement();
            boundaryEvent.setBehavior(bpmnParse.getActivityBehaviorFactory().createBoundaryTimerEventActivityBehavior(boundaryEvent, timerEventDefinition, boundaryEvent.isCancelActivity()));
        }

    }
}
