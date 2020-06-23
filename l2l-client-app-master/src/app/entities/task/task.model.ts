import { PaginationModel } from '../page.model';

export interface TaskModel {
  appName?: string;
  appVersion?: string;
  serviceName?: string;
  serviceFullName?: string;
  serviceType?: string;
  serviceVersion?: string;
  id?: string;
  assignee?: string;
  name?: string;
  createdDate?: string;
  priority?: string;
  processDefinitionId?: string;
  processInstanceId?: string;
  status?: string;
}
export interface TaskEntry {
  entry: TaskModel;
}

export interface TasksResponse {
  list: {
    entries: TaskEntry [];
    pagination: PaginationModel;
  };
}


export class Task implements  TaskModel {
  constructor(
    public id?: string,
    public appName?: string,
    public appVersion?: string,
    public serviceName?: string,
    public serviceFullName?: string,
    public serviceType?: string,
    public serviceVersion?: string,
    public version?: string,
    public assignee?: string,
    public name?: string,
    public createdDate?: string,
    public priority?: string,
    public processDefinitionId?: string,
    public processInstanceId?: string,
    public status?: string
  ) {
    this.id = id ? id : null;
    this.appName = appName ? appName : null;
    this.appVersion = appVersion ? appVersion : null;
    this.serviceName = serviceName ? serviceName : null;
    this.serviceFullName = serviceFullName ? serviceFullName : null;
    this.serviceType = serviceType ? serviceType : null;
    this.serviceVersion = serviceVersion ? serviceVersion : null;
    this.assignee = assignee ? assignee : null;
    this.name = name ? name : null;
    this.createdDate = createdDate ? createdDate : null;
    this.priority = priority ? priority : null;
    this.processDefinitionId = processDefinitionId ? processDefinitionId : null;
    this.processInstanceId = processInstanceId ? processInstanceId : null;
    this.status = status ? status : null;
  }
}

