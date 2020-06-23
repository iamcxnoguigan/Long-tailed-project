package com.l2l.enterprise.vessel.extension.activiti.behavior;

import org.activiti.bpmn.model.BoundaryEvent;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.bpmn.model.TimerEventDefinition;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.BoundaryTimerEventActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.delegate.ActivityBehavior;

public class L2LActivityBehaviorFactory extends DefaultActivityBehaviorFactory {
    public BoundaryTimerEventActivityBehavior createBoundaryTimerEventActivityBehavior(BoundaryEvent boundaryEvent, TimerEventDefinition timerEventDefinition, boolean interrupting) {
        return new L2LBoundaryTimerEventActivityBehavior(timerEventDefinition, interrupting);
    }
}
