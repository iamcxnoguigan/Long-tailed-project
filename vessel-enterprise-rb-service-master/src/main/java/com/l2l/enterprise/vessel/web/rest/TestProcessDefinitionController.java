package com.l2l.enterprise.vessel.web.rest;

import org.activiti.cloud.services.rest.api.resources.ProcessDefinitionResource;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(
    value = {"/v2/process-definitions"},
    produces = {"application/hal+json", "application/json"}
)
public interface TestProcessDefinitionController {
    @RequestMapping(
        method = {RequestMethod.GET}
    )
    PagedResources<ProcessDefinitionResource> getProcessDefinitions(Pageable var1);

    @RequestMapping(
        value = {"/{id}"},
        method = {RequestMethod.GET}
    )
    ProcessDefinitionResource getProcessDefinition(@PathVariable String var1);

    @RequestMapping(
        value = {"/{id}/model"},
        method = {RequestMethod.GET},
        produces = {"application/xml"}
    )
    @ResponseBody
    String getProcessModel(@PathVariable String var1);

    @RequestMapping(
        value = {"/{id}/model"},
        method = {RequestMethod.GET},
        produces = {"application/json"}
    )
    @ResponseBody
    String getBpmnModel(@PathVariable String var1);

    @RequestMapping(
        value = {"/{id}/model"},
        method = {RequestMethod.GET},
        produces = {"image/svg+xml"}
    )
    @ResponseBody
    String getProcessDiagram(@PathVariable String var1);
}
