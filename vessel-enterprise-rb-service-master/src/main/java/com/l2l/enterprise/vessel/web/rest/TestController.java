package com.l2l.enterprise.vessel.web.rest;

import com.l2l.enterprise.vessel.extension.activiti.form.FormDefinition;
import com.l2l.enterprise.vessel.extension.activiti.form.FormService;
import com.l2l.enterprise.vessel.service.L2LTaskRuntimeImpl;
import org.activiti.cloud.alfresco.data.domain.AlfrescoPagedResourcesAssembler;
import org.activiti.cloud.services.core.ActivitiForbiddenException;
import org.activiti.cloud.services.core.ProcessDiagramGeneratorWrapper;
import org.activiti.cloud.services.core.pageable.SpringPageConverter;
import org.activiti.cloud.services.rest.api.resources.ProcessInstanceResource;
import org.activiti.cloud.services.rest.api.resources.TaskResource;
import org.activiti.cloud.services.rest.assemblers.ProcessInstanceResourceAssembler;
import org.activiti.cloud.services.rest.assemblers.TaskResourceAssembler;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.exception.ActivitiInterchangeInfoNotFoundException;
import org.activiti.runtime.api.NotFoundException;
import org.activiti.runtime.api.ProcessRuntime;
import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.Task;
import org.activiti.runtime.api.model.builders.TaskPayloadBuilder;
import org.activiti.runtime.api.model.payloads.CompleteTaskPayload;
import org.activiti.runtime.api.model.payloads.StartProcessPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@RestController
public class TestController {
    private final RepositoryService repositoryService;
    private final ProcessDiagramGeneratorWrapper processDiagramGenerator;
    private final ProcessInstanceResourceAssembler resourceAssembler;
    private final AlfrescoPagedResourcesAssembler<Task> taskpagedResourcesAssembler;
    private final AlfrescoPagedResourcesAssembler<ProcessInstance> pagedResourcesAssembler;
    private final ProcessRuntime processRuntime;
    private final SpringPageConverter pageConverter;
//    private final RepositoryService;
    @Autowired
    private FormService formService;
    @ExceptionHandler({ActivitiForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAppException(ActivitiForbiddenException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({ActivitiObjectNotFoundException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAppException(RuntimeException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({ActivitiInterchangeInfoNotFoundException.class})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String handleActivitiInterchangeInfoNotFoundException(ActivitiInterchangeInfoNotFoundException ex) {
        return ex.getMessage();
    }
    Logger logger = Logger.getLogger("Log");

    @Autowired
    public TestController(RepositoryService repositoryService, ProcessDiagramGeneratorWrapper processDiagramGenerator, ProcessInstanceResourceAssembler resourceAssembler, AlfrescoPagedResourcesAssembler<Task> taskpagedResourcesAssembler, AlfrescoPagedResourcesAssembler<ProcessInstance> pagedResourcesAssembler, ProcessRuntime processRuntime, SpringPageConverter pageConverter) {
        this.repositoryService = repositoryService;
        this.processDiagramGenerator = processDiagramGenerator;
        this.resourceAssembler = resourceAssembler;
        this.taskpagedResourcesAssembler = taskpagedResourcesAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.processRuntime = processRuntime;
        this.pageConverter = pageConverter;
    }
    @RequestMapping(
        value = {"/v2/process-instances/{processDefinitionId}/{key}"},
        method = {RequestMethod.POST}
    )
    public ProcessInstanceResource testHello(@PathVariable("processDefinitionId") String processDefinitionId,@PathVariable("key") String key){
        String processDefinitionKey = "myProcess_1";
        String processInstanceName = "myProcess_1";
        StartProcessPayload startProcessPayload = new StartProcessPayload(processDefinitionId , key , key , null , null);
        return this.resourceAssembler.toResource(this.processRuntime.start(startProcessPayload));
    }

    @RequestMapping(
        value = {"/v2/process-instances/withvar/{processDefinitionId}"},
        method = {RequestMethod.POST}
    )
    public ProcessInstanceResource startProcessinstanceWithVariables(@PathVariable("processDefinitionId") String processDefinitionId ,@RequestBody Map<String,Object> varMap){
        String processDefinitionKey = "myProcess_1";
        String processInstanceName = "myProcess_1";
        StartProcessPayload startProcessPayload = new StartProcessPayload(processDefinitionId , processDefinitionKey , processInstanceName , null , varMap);
        ProcessInstance s = this.processRuntime.start(startProcessPayload);
        System.out.println(s.getId());
        return this.resourceAssembler.toResource(s);
    }


    @Autowired
    L2LTaskRuntimeImpl l2LTaskRuntime;
    @Autowired
    TaskResourceAssembler taskResourceAssembler;
    @RequestMapping(
        value = {"v2/{taskId}/complete"},
        method = {RequestMethod.POST}
    )
    public TaskResource completeTask(@PathVariable String taskId, @RequestBody(required = false) Map<String,Object> map,CompleteTaskPayload completeTaskPayload) {
        if (completeTaskPayload == null) {
            completeTaskPayload = TaskPayloadBuilder.complete().withTaskId(taskId).build();
        } else {
            completeTaskPayload.setTaskId(taskId);
        }
        completeTaskPayload.setVariables(map);
        Task task = this.l2LTaskRuntime.complete(completeTaskPayload);
        return this.taskResourceAssembler.toResource(task);
    }

    @RequestMapping(
        value = {"/v2/startform/{processDefinitionId}"},
        method = {RequestMethod.GET}
    )
    public FormDefinition getStartFormDefinition(@PathVariable("processDefinitionId") String processDefinitionId){
        return this.formService.getStartForm(processDefinitionId);
    }
    @Autowired
    TaskService taskService;
    @RequestMapping("/v2/gettaskform/{taskId}")
    public FormDefinition gettaskForm(@PathVariable("taskId") String taskId){
        org.activiti.engine.task.Task internalTask = (org.activiti.engine.task.Task)((TaskQuery)this.taskService.createTaskQuery().taskId(taskId)).singleResult();
        String taskDefinitionKey = internalTask.getTaskDefinitionKey();
        String processDefinitionId = internalTask.getProcessDefinitionId();
        return this.formService.getUserTaskForm(processDefinitionId,taskDefinitionKey);
    }
    @Autowired
    SenderTest senderTest;
    @RequestMapping("/sender")
    public void sender(){
        senderTest.sinkSenderTester();
    }
    @Autowired
    BpmnAdd bpmnAdd;
    @RequestMapping(
        value = {"/v2/bpmn"},
        method = {RequestMethod.POST}
    )
    public String bpmnFileChange(@RequestBody Map<String,String> fileMap) throws IOException {
        bpmnAdd.changBpmn(fileMap.get("oldfile"),fileMap.get("newfile"));
        return "ok";
    }
    @Autowired
    h2Jdbc h2Jdbc;
    @RequestMapping(
        value = {"/v2/process-instances/tasks/{processInstanceId}"},
        method = {RequestMethod.GET},
        produces = {"application/hal+json", "application/json"}
    )
    public HashMap<String, List<String>> getTasks(@PathVariable String processInstanceId) throws Exception {
        HashMap<String, List<String>> taskid = h2Jdbc.querytask();

        return taskid;
    }
// 松饼例子函数与接口
    public void start(String CustomerID,String MyMuffinID,String LogisticsID) throws Exception {
        //首先启动customer 1.完成make order 2.启动Mymuffin 完成setup delivery 3.启动logistic 完成arrange for delivery
        String processDefinitionKey = "customer";
        String processInstanceName = "customer";
    //启动Customer流程
        StartProcessPayload startProcessPayload = new StartProcessPayload(CustomerID , processDefinitionKey , processInstanceName , null , null);
        String pinstancceId = this.processRuntime.start(startProcessPayload).getId();
        List<String> nowTask = h2Jdbc.finTaskIf(pinstancceId);

    }

    public void completeTask(String userName,String taskId,String taskName){
        logger.info("当前用户:"+ userName+" " + "当前任务：" + taskName);

    }

}
