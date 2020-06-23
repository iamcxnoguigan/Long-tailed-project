import { PaginationModel } from '../page.model';

export interface ProcessInstance {
    id?: string;
    applicationName?: string;
    initiator?: string;
    processDefinitionId?: string;
    processDefinitionKey?: string;
    startDate?: string;
    status?: string;
    lastModified?: string;
}

export interface ProcessInstanceEntry {
  entry: ProcessInstance;
}

export interface ProcessInstancesResponse {
  list: {
    entries: ProcessInstanceEntry [];
    pagination: PaginationModel;
  };
}
