package org.activiti.cloud.starter.rb.extension.annotation;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.cloud.starter.rb.extension.model.Annotation;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;


import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class GetActivityAnnotationsCmd implements Command<List<Annotation>>, Serializable {
    protected String processDefinitionId;
    protected String targetElementId;
    public GetActivityAnnotationsCmd(String processDefinitionId , String targetElementId){
        this.processDefinitionId = processDefinitionId;
        this.targetElementId = targetElementId;
    }
    @Override
    public List<Annotation> execute(CommandContext commandContext) {
        if(processDefinitionId == null){
            throw new ActivitiIllegalArgumentException("processDefinitionId is null");

        }else{
            Process process = ProcessDefinitionUtil.getProcess(this.processDefinitionId);
            FlowElement flowElement = process.getFlowElement(this.targetElementId);
            List<Annotation> annotations = AnnotationUtils.collectAnnotationsOnElement(flowElement);
            List<Annotation> res = annotations.stream().map(an -> {
                an.setProcessDefinitionId(processDefinitionId);
                an.setTargetElementId(targetElementId); return an;}).collect(Collectors.toList());

            return  res;
        }
    }
}
