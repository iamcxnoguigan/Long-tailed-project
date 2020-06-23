import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import 'rxjs/add/operator/map';
import {SERVER_API_URL} from 'src/app/app.constants';
import {createRequestOption} from '../../shared/util/request-util';
import {TaskModel, TasksResponse} from './task.model';

@Injectable()
export class TaskService {


  constructor(
    private http: HttpClient) {
  }

  queryAll(runtimeBundle: string , req?: any ): Observable<HttpResponse<TasksResponse>> {
    const options = createRequestOption(req);
    return this.http.get<TasksResponse>(SERVER_API_URL + `/${runtimeBundle}/v1/tasks`,
      {params: options, observe : 'response'});
  }
  queryOne(runtimeBundle: string , taskId: string ): Observable<HttpResponse<TaskModel>> {
    return this.http.get<TaskModel>(SERVER_API_URL + `/${runtimeBundle}/v1/tasks/${taskId}`,
      {observe : 'response'});
  }

  // complete(runtimeBundle: string, taskId: string) {
  //   if (!!runtimeBundle && !!taskId) {
  //     return this.http.post(SERVER_API_URL +  + `/${runtimeBundle}/v1/tasks/${processInstanceId}/complete`,
  //       undefined);
  //   }
  // }
}
