package com.l2l.enterprise.vessel.extension.activiti.annotation;

import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class GetAllActivitiesAnnotationsCmd implements Command<List<Annotation>>, Serializable {
    private static Logger log = LoggerFactory.getLogger(GetAllActivitiesAnnotationsCmd.class);

    protected String processDefinitionId;
    public GetAllActivitiesAnnotationsCmd(String processDefinitionId){
        this.processDefinitionId = processDefinitionId;
    }
    @Override
    public List<Annotation> execute(CommandContext commandContext) {
        if(processDefinitionId == null){
            throw new ActivitiIllegalArgumentException("processDefinitionId is null");

        }else{
            Process process = ProcessDefinitionUtil.getProcess(this.processDefinitionId);
            Collection<FlowElement> flowElements = process.getFlowElements();
            Iterator<FlowElement> it = flowElements.iterator();
            List<Annotation> res = new ArrayList<Annotation>();
            while (it.hasNext()){
                FlowElement tFe = it.next();
                if(tFe instanceof SubProcess){
                    log.debug("subprocess is unsupported");
                }
                else{
                    List<Annotation> annotations = AnnotationUtils.collectAnnotationsOnElement(tFe);
                    List<Annotation> tRes = annotations.stream().map(an -> {
                        an.setProcessDefinitionId(processDefinitionId);
                        an.setTargetElementId(tFe.getId()); return an;}).collect(Collectors.toList());
                    res.addAll(tRes);
                }

            }

            return res;
        }
    }
}
