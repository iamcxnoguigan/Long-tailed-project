import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
import { SERVER_API_URL } from 'src/app/app.constants';
import {StartProcessPayload} from './process-definition.model';
import {ProcessInstance} from '../process-instance/process-instance.model';

@Injectable()
export class ProcessStartService {

  constructor(
    private http: HttpClient) {
  }

  start(runtimeBundle: string , startProcessPayload: StartProcessPayload):
  Observable<HttpResponse<ProcessInstance>> {
    return this.http.post(SERVER_API_URL + `/${runtimeBundle}/v1/process-instances`, startProcessPayload ,
      { observe: 'response' });
  }
}
