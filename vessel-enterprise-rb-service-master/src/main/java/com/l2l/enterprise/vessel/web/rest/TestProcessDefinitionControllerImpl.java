package com.l2l.enterprise.vessel.web.rest;

import org.activiti.cloud.alfresco.data.domain.AlfrescoPagedResourcesAssembler;
import org.activiti.cloud.services.core.ProcessDiagramGeneratorWrapper;
import org.activiti.cloud.services.core.pageable.SpringPageConverter;
import org.activiti.cloud.services.rest.api.resources.ProcessDefinitionResource;
import org.activiti.cloud.services.rest.assemblers.ProcessDefinitionResourceAssembler;
import org.activiti.engine.RepositoryService;
import org.activiti.runtime.api.ProcessRuntime;
import org.activiti.runtime.api.model.ProcessDefinition;
import org.activiti.runtime.api.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestProcessDefinitionControllerImpl implements TestProcessDefinitionController {

    private final RepositoryService repositoryService;
    private final ProcessDiagramGeneratorWrapper processDiagramGenerator;
    private final ProcessDefinitionResourceAssembler resourceAssembler;
    private final ProcessRuntime processRuntime;
    private final AlfrescoPagedResourcesAssembler<ProcessDefinition> pagedResourcesAssembler;
    private final SpringPageConverter pageConverter;

    @Autowired
    public TestProcessDefinitionControllerImpl(RepositoryService repositoryService, ProcessDiagramGeneratorWrapper processDiagramGenerator, ProcessDefinitionResourceAssembler resourceAssembler, @Qualifier(value="testProcessRuntime") ProcessRuntime processRuntime, AlfrescoPagedResourcesAssembler<ProcessDefinition> pagedResourcesAssembler, SpringPageConverter pageConverter) {
        this.repositoryService = repositoryService;
        this.processDiagramGenerator = processDiagramGenerator;
        this.resourceAssembler = resourceAssembler;
        this.processRuntime = processRuntime;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.pageConverter = pageConverter;
    }
    @Override
    public PagedResources<ProcessDefinitionResource> getProcessDefinitions(Pageable pageable) {
        Page<ProcessDefinition> page = this.processRuntime.processDefinitions(this.pageConverter.toAPIPageable(pageable));
        return this.pagedResourcesAssembler.toResource(pageable, this.pageConverter.toSpringPage(pageable, page), this.resourceAssembler);
    }

    @Override
    public ProcessDefinitionResource getProcessDefinition(@PathVariable String id) {
        return this.resourceAssembler.toResource(this.processRuntime.processDefinition(id));
    }

    @Override
    public String getProcessModel(String s) {
        return null;
    }

    @Override
    public String getBpmnModel(String s) {
        return null;
    }

    @Override
    public String getProcessDiagram(String s) {
        return null;
    }
}
