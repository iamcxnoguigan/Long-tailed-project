package com.l2l.enterprise.vessel.extension.activiti.annotation;

import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import com.l2l.enterprise.vessel.extension.activiti.parser.AnnotationConstants;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GetMsgAnnotationsCmd implements Command<List<MsgAnnotation>>, Serializable {
    private static Logger log = LoggerFactory.getLogger(GetMsgAnnotationsCmd.class);
    protected String processDefinitionId;
    public GetMsgAnnotationsCmd(String processDefinitionId){
        this.processDefinitionId = processDefinitionId;
    }
    @Override
    public List<MsgAnnotation> execute(CommandContext commandContext) {
        if(processDefinitionId == null){
            throw new ActivitiIllegalArgumentException("processDefinitionId is null");

        }else{
            Process process = ProcessDefinitionUtil.getProcess(this.processDefinitionId);
//            List<ServiceTask> serviceTasks = process.findFlowElementsOfType(Process.class);
            Collection<FlowElement> flowElements = process.getFlowElements();
            Iterator<FlowElement> it = flowElements.iterator();
            List<MsgAnnotation> res = new ArrayList<MsgAnnotation>();
            List<MsgAnnotation> msgannotations = AnnotationUtils.collectMsgAnnoationOnProcess(process);
            List<MsgAnnotation> tRes = msgannotations.stream().map(an -> {
                an.setProcessDefinitionId(processDefinitionId);
                an.setTargetElementId(process.getId()); return an;}).collect(Collectors.toList());
            res.addAll(tRes);
//            while (it.hasNext()){
//                FlowElement tFe = it.next();
//                if(tFe instanceof Process){
//                    List<MsgAnnotation> msgannotations = AnnotationUtils.collectMsgAnnoationOnProcess(tFe);
//                    List<MsgAnnotation> tRes = msgannotations.stream().map(an -> {
//                        an.setProcessDefinitionId(processDefinitionId);
//                        an.setTargetElementId(tFe.getId()); return an;}).collect(Collectors.toList());
//                    res.addAll(tRes);
//                }else if(tFe instanceof SubProcess){
//                    log.debug("subprocess is unsupported");
//                }else {
//                    log.debug("not msgAnnotation");
//                }
////                    List<Annotation> annotations = AnnotationUtils.collectAnnotationsOnElement(tFe);
////                    List<Annotation> tRes = annotations.stream().map(an -> {
////                        an.setProcessDefinitionId(processDefinitionId);
////                        an.setTargetElementId(tFe.getId()); return an;}).collect(Collectors.toList());
////                    res.addAll(tRes);
////                }
//
//            }
            return  res;
        }
    }
}
