import {ActivatedRouteSnapshot, Resolve, Route, RouterStateSnapshot, Routes} from '@angular/router';
import { TaskComponent } from './task.component';
import { JhiResolvePagingParams } from 'ng-jhipster';
import {Injectable} from '@angular/core';
import {TaskService} from './task.service';
import { Task } from './task.model';
import {TaskCompleteComponent} from './task-complete.component';

@Injectable({ providedIn: 'root' })
export class TaskResolve implements Resolve<any> {
  constructor(private service: TaskService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params['taskId'] ? route.params['taskId'] : null;
    console.log('id ', id);
    if (id) {
      return this.service.queryOne('api-vessel-rb', id);
    }
    return new Task();
  }
}
export const  taskRoutes: Routes = [
  {
    path: 'task',
    component: TaskComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      pageTitle: 'runtimeBundle.title',
      defaultSort: 'id,asc',
      runtimeBundle: 'api-vessel-rb'
    }
  },
  {
    path: 'task/:taskId/complete',
    component: TaskCompleteComponent,
    resolve: {
      task : TaskResolve
    },
    data: {
      pageTitle: 'runtimeBundle.title',
      defaultSort: 'id,asc',
      runtimeBundle: 'api-vessel-rb'
    }
  }
];
