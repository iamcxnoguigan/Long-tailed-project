package com.l2l.enterprise.vessel.service;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.TaskQuery;
import org.activiti.runtime.api.NotFoundException;
import org.activiti.runtime.api.TaskRuntime;
import org.activiti.runtime.api.conf.TaskRuntimeConfiguration;
import org.activiti.runtime.api.identity.UserGroupManager;
import org.activiti.runtime.api.model.Task;
import org.activiti.runtime.api.model.VariableInstance;
import org.activiti.runtime.api.model.builders.TaskPayloadBuilder;
import org.activiti.runtime.api.model.impl.APITaskConverter;
import org.activiti.runtime.api.model.impl.APIVariableInstanceConverter;
import org.activiti.runtime.api.model.impl.TaskImpl;
import org.activiti.runtime.api.model.payloads.*;
import org.activiti.runtime.api.query.Page;
import org.activiti.runtime.api.query.Pageable;
import org.activiti.runtime.api.query.impl.PageImpl;
import org.activiti.runtime.api.security.SecurityManager;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
@Component
public class L2LTaskRuntimeImpl implements TaskRuntime {
    private final TaskService taskService;
    private final APITaskConverter taskConverter;
    private final APIVariableInstanceConverter variableInstanceConverter;
    private final TaskRuntimeConfiguration configuration;
    private final UserGroupManager userGroupManager;
    private final SecurityManager securityManager;

    public L2LTaskRuntimeImpl(TaskService taskService, UserGroupManager userGroupManager, SecurityManager securityManager, APITaskConverter taskConverter, APIVariableInstanceConverter variableInstanceConverter, TaskRuntimeConfiguration configuration) {
        this.taskService = taskService;
        this.userGroupManager = userGroupManager;
        this.securityManager = securityManager;
        this.taskConverter = taskConverter;
        this.variableInstanceConverter = variableInstanceConverter;
        this.configuration = configuration;
    }

    public TaskRuntimeConfiguration configuration() {
        return this.configuration;
    }

    public Task task(String taskId) {
        String authenticatedUserId = this.securityManager.getAuthenticatedUserId();
        if (authenticatedUserId != null && !authenticatedUserId.isEmpty()) {
            List<String> userRoles = this.userGroupManager.getUserRoles(authenticatedUserId);
            List<String> userGroups = this.userGroupManager.getUserGroups(authenticatedUserId);
            org.activiti.engine.task.Task internalTask = (org.activiti.engine.task.Task)((TaskQuery)this.taskService.createTaskQuery().taskCandidateOrAssigned(authenticatedUserId, userGroups).taskId(taskId)).singleResult();
            if (internalTask == null) {
                throw new NotFoundException("Unable to find task for the given id: " + taskId + " for user: " + authenticatedUserId + " (with groups: " + userGroups + " & with roles: " + userRoles + ")");
            } else {
//                internalTask.getTaskDefinitionKey();
                return this.taskConverter.from(internalTask);
            }

        } else {
            throw new IllegalStateException("There is no authenticated user, we need a user authenticated to find tasks");
        }
    }

    public Page<Task> tasks(Pageable pageable) {
        String authenticatedUserId = this.securityManager.getAuthenticatedUserId();
        if (authenticatedUserId != null && !authenticatedUserId.isEmpty()) {
            List<String> userGroups = this.userGroupManager.getUserGroups(authenticatedUserId);
            return this.tasks(pageable, TaskPayloadBuilder.tasks().withAssignee(authenticatedUserId).withGroups(userGroups).build());
        } else {
            throw new IllegalStateException("You need an authenticated user to perform a task query");
        }
    }

    public Page<Task> tasks(Pageable pageable, GetTasksPayload getTasksPayload) {
        TaskQuery taskQuery = this.taskService.createTaskQuery();
        if (getTasksPayload == null) {
            getTasksPayload = TaskPayloadBuilder.tasks().build();
        }

        String authenticatedUserId = this.securityManager.getAuthenticatedUserId();
        if (authenticatedUserId != null && !authenticatedUserId.isEmpty()) {
            List<String> task = this.userGroupManager.getUserGroups(authenticatedUserId);
            getTasksPayload.setAssigneeId(authenticatedUserId);
            getTasksPayload.setGroups(task);
            taskQuery = (TaskQuery)((TaskQuery)((TaskQuery)taskQuery.or()).taskCandidateOrAssigned(getTasksPayload.getAssigneeId(), getTasksPayload.getGroups()).taskOwner(authenticatedUserId)).endOr();
            if (getTasksPayload.getProcessInstanceId() != null) {
                taskQuery = (TaskQuery)taskQuery.processInstanceId(getTasksPayload.getProcessInstanceId());
            }

            if (getTasksPayload.getParentTaskId() != null) {
                taskQuery = (TaskQuery)taskQuery.taskParentTaskId(getTasksPayload.getParentTaskId());
            }

            List<Task> tasks = this.taskConverter.from(taskQuery.listPage(pageable.getStartIndex(), pageable.getMaxItems()));
            return new PageImpl(tasks, Math.toIntExact(taskQuery.count()));
        } else {
            throw new IllegalStateException("You need an authenticated user to perform a task query");
        }
    }

    public Page<Task> tasksnopage() {
        String authenticatedUserId = this.securityManager.getAuthenticatedUserId();
        if (authenticatedUserId != null && !authenticatedUserId.isEmpty()) {
            List<String> userGroups = this.userGroupManager.getUserGroups(authenticatedUserId);
            return this.tasksnopage(TaskPayloadBuilder.tasks().withAssignee(authenticatedUserId).withGroups(userGroups).build());
        } else {
            throw new IllegalStateException("You need an authenticated user to perform a task query");
        }
    }

    public Page<Task> tasksnopage(GetTasksPayload getTasksPayload) {
        TaskQuery taskQuery = this.taskService.createTaskQuery();
        if (getTasksPayload == null) {
            getTasksPayload = TaskPayloadBuilder.tasks().build();
        }

        String authenticatedUserId = this.securityManager.getAuthenticatedUserId();
        if (authenticatedUserId != null && !authenticatedUserId.isEmpty()) {
            List<String> task = this.userGroupManager.getUserGroups(authenticatedUserId);
            getTasksPayload.setAssigneeId(authenticatedUserId);
            getTasksPayload.setGroups(task);
            taskQuery = (TaskQuery)((TaskQuery)((TaskQuery)taskQuery.or()).taskCandidateOrAssigned(getTasksPayload.getAssigneeId(), getTasksPayload.getGroups()).taskOwner(authenticatedUserId)).endOr();
            if (getTasksPayload.getProcessInstanceId() != null) {
                taskQuery = (TaskQuery)taskQuery.processInstanceId(getTasksPayload.getProcessInstanceId());
            }

            if (getTasksPayload.getParentTaskId() != null) {
                taskQuery = (TaskQuery)taskQuery.taskParentTaskId(getTasksPayload.getParentTaskId());
            }

            List<Task> tasks = this.taskConverter.from(taskQuery.listPage(0,10));
            return new PageImpl(tasks, Math.toIntExact(taskQuery.count()));
        } else {
            throw new IllegalStateException("You need an authenticated user to perform a task query");
        }
    }

    public List<VariableInstance> variables(GetTaskVariablesPayload getTaskVariablesPayload) {
        return getTaskVariablesPayload.isLocalOnly() ? this.variableInstanceConverter.from(this.taskService.getVariableInstancesLocal(getTaskVariablesPayload.getTaskId()).values()) : this.variableInstanceConverter.from(this.taskService.getVariableInstances(getTaskVariablesPayload.getTaskId()).values());
    }

    public Task complete(CompleteTaskPayload completeTaskPayload) {
        String authenticatedUserId = this.securityManager.getAuthenticatedUserId();

        Task task;
        try {
            task = this.task(completeTaskPayload.getTaskId());
        } catch (IllegalStateException var5) {
            throw new IllegalStateException("The authenticated user cannot complete task" + completeTaskPayload.getTaskId() + " due he/she cannot access to the task");
        }

        if (task.getAssignee() != null && !task.getAssignee().isEmpty()) {
            if (!task.getAssignee().equals(authenticatedUserId)) {
                throw new IllegalStateException("You cannot complete the task if you are not assigned to it");
            } else {
                TaskImpl competedTaskData = new TaskImpl(task.getId(), task.getName(), Task.TaskStatus.COMPLETED);
                this.taskService.complete(completeTaskPayload.getTaskId(), completeTaskPayload.getVariables());
                return competedTaskData;
            }
        } else {
            throw new IllegalStateException("The task needs to be claimed before trying to complete it");
        }
    }

    public Task claim(ClaimTaskPayload claimTaskPayload) {
        Task task;
        try {
            task = this.task(claimTaskPayload.getTaskId());
        } catch (IllegalStateException var4) {
            throw new IllegalStateException("The authenticated user cannot claim task" + claimTaskPayload.getTaskId() + " due it is not a candidate for it");
        }

        if (task.getAssignee() != null && !task.getAssignee().isEmpty()) {
            throw new IllegalStateException("The task was already claimed, the assignee of this task needs to release it first for you to claim it");
        } else {
            String authenticatedUserId = this.securityManager.getAuthenticatedUserId();
            claimTaskPayload.setAssignee(authenticatedUserId);
            this.taskService.claim(claimTaskPayload.getTaskId(), claimTaskPayload.getAssignee());
            return this.task(claimTaskPayload.getTaskId());
        }
    }

    public Task release(ReleaseTaskPayload releaseTaskPayload) {
        Task task;
        try {
            task = this.task(releaseTaskPayload.getTaskId());
        } catch (IllegalStateException var4) {
            throw new IllegalStateException("The authenticated user cannot claim task" + releaseTaskPayload.getTaskId() + " due it is not a candidate for it");
        }

        if (task.getAssignee() != null && !task.getAssignee().isEmpty()) {
            String authenticatedUserId = this.securityManager.getAuthenticatedUserId();
            if (!task.getAssignee().equals(authenticatedUserId)) {
                throw new IllegalStateException("You cannot release a task where you are not the assignee");
            } else {
                this.taskService.unclaim(releaseTaskPayload.getTaskId());
                return this.task(releaseTaskPayload.getTaskId());
            }
        } else {
            throw new IllegalStateException("You cannot release a task that is not claimed");
        }
    }

    public Task update(UpdateTaskPayload updateTaskPayload) {
        Task task;
        try {
            task = this.task(updateTaskPayload.getTaskId());
        } catch (IllegalStateException var5) {
            throw new IllegalStateException("The authenticated user cannot update the task" + updateTaskPayload.getTaskId() + " due it is not the current assignee");
        }

        String authenticatedUserId = this.securityManager.getAuthenticatedUserId();
        if (!Objects.equals(task.getAssignee(), authenticatedUserId)) {
            throw new IllegalStateException("You cannot update a task where you are not the assignee");
        } else {
            org.activiti.engine.task.Task internalTask = this.getInternalTask(updateTaskPayload.getTaskId());
            if (updateTaskPayload.getTaskName() != null) {
                internalTask.setName(updateTaskPayload.getTaskName());
            }

            if (updateTaskPayload.getDescription() != null) {
                internalTask.setDescription(updateTaskPayload.getDescription());
            }

            if (updateTaskPayload.getPriority() != null) {
                internalTask.setPriority(updateTaskPayload.getPriority());
            }

            if (updateTaskPayload.getAssignee() != null) {
                internalTask.setAssignee(updateTaskPayload.getAssignee());
            }

            if (updateTaskPayload.getDueDate() != null) {
                internalTask.setDueDate(updateTaskPayload.getDueDate());
            }

            this.taskService.saveTask(internalTask);
            return this.task(updateTaskPayload.getTaskId());
        }
    }

    public Task delete(DeleteTaskPayload deleteTaskPayload) {
        Task task;
        try {
            task = this.task(deleteTaskPayload.getTaskId());
        } catch (IllegalStateException var5) {
            throw new IllegalStateException("The authenticated user cannot delete the task" + deleteTaskPayload.getTaskId() + " due it is not the current assignee");
        }

        String authenticatedUserId = this.securityManager.getAuthenticatedUserId();
        if ((task.getAssignee() == null || task.getAssignee().isEmpty() || !task.getAssignee().equals(authenticatedUserId)) && (task.getOwner() == null || task.getOwner().isEmpty() || !task.getOwner().equals(authenticatedUserId))) {
            throw new IllegalStateException("You cannot delete a task where you are not the assignee/owner");
        } else {
            TaskImpl deletedTaskData = new TaskImpl(task.getId(), task.getName(), Task.TaskStatus.DELETED);
            if (!deleteTaskPayload.hasReason()) {
                deleteTaskPayload.setReason("Cancelled by " + authenticatedUserId);
            }

            this.taskService.deleteTask(deleteTaskPayload.getTaskId(), deleteTaskPayload.getReason(), true);
            return deletedTaskData;
        }
    }

    public Task create(CreateTaskPayload createTaskPayload) {
        org.activiti.engine.task.Task task = this.taskService.newTask();
        task.setName(createTaskPayload.getName());
        task.setDescription(createTaskPayload.getDescription());
        task.setDueDate(createTaskPayload.getDueDate());
        task.setPriority(createTaskPayload.getPriority());
        if (createTaskPayload.getAssignee() != null && !createTaskPayload.getAssignee().isEmpty()) {
            task.setAssignee(createTaskPayload.getAssignee());
        }

        task.setParentTaskId(createTaskPayload.getParentTaskId());
        task.setOwner(this.securityManager.getAuthenticatedUserId());
        this.taskService.saveTask(task);
        this.taskService.addCandidateUser(task.getId(), this.securityManager.getAuthenticatedUserId());
        if (createTaskPayload.getGroups() != null && !createTaskPayload.getGroups().isEmpty()) {
            Iterator var3 = createTaskPayload.getGroups().iterator();

            while(var3.hasNext()) {
                String g = (String)var3.next();
                this.taskService.addCandidateGroup(task.getId(), g);
            }
        }

        return this.taskConverter.from(task);
    }

    public void setVariables(SetTaskVariablesPayload setTaskVariablesPayload) {
        if (setTaskVariablesPayload.isLocalOnly()) {
            this.taskService.setVariablesLocal(setTaskVariablesPayload.getTaskId(), setTaskVariablesPayload.getVariables());
        } else {
            this.taskService.setVariables(setTaskVariablesPayload.getTaskId(), setTaskVariablesPayload.getVariables());
        }

    }

    private org.activiti.engine.task.Task getInternalTask(String taskId) {
        org.activiti.engine.task.Task internalTask = (org.activiti.engine.task.Task)((TaskQuery)this.taskService.createTaskQuery().taskId(taskId)).singleResult();
        if (internalTask == null) {
            throw new NotFoundException("Unable to find task for the given id: " + taskId);
        } else {
            return internalTask;
        }
    }
}
