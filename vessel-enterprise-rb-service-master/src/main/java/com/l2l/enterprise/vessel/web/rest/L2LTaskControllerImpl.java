package com.l2l.enterprise.vessel.web.rest;

import com.l2l.enterprise.vessel.service.L2LTaskRuntimeImpl;
import org.activiti.cloud.alfresco.data.domain.AlfrescoPagedResourcesAssembler;
import org.activiti.cloud.services.core.pageable.SpringPageConverter;
import org.activiti.cloud.services.rest.api.TaskController;
import org.activiti.cloud.services.rest.api.resources.TaskResource;
import org.activiti.cloud.services.rest.assemblers.TaskResourceAssembler;
import org.activiti.runtime.api.TaskRuntime;
import org.activiti.runtime.api.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(
    value = {"/v2/tasks"},
    produces = {"application/hal+json", "application/json"}
)
@RestController
public class L2LTaskControllerImpl {
    private final TaskResourceAssembler taskResourceAssembler;
    private final AlfrescoPagedResourcesAssembler<Task> pagedResourcesAssembler;
    private final SpringPageConverter pageConverter;
    private final TaskRuntime taskRuntime;

    @Autowired
    public L2LTaskControllerImpl(TaskResourceAssembler taskResourceAssembler, AlfrescoPagedResourcesAssembler<Task> pagedResourcesAssembler, SpringPageConverter pageConverter, @Qualifier("l2LTaskRuntimeImpl") TaskRuntime taskRuntime) {
        this.taskResourceAssembler = taskResourceAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.pageConverter = pageConverter;
        this.taskRuntime = taskRuntime;
    }

    @RequestMapping(
        value = {"/{taskId}"},
        method = {RequestMethod.GET}
    )
    public TaskResource getTaskById(@PathVariable String taskId) {
        Task task = this.taskRuntime.task(taskId);
        return this.taskResourceAssembler.toResource(task);
    }

}
