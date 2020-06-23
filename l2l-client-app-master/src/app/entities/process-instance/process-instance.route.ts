import { Route } from '@angular/router';
import { ProcessInstanceComponent } from './process-instance.component';
import { JhiResolvePagingParams } from 'ng-jhipster';


export const processInstanceRoute: Route = {
  path: 'process-instances',
  component: ProcessInstanceComponent,
  resolve: {
    pagingParams: JhiResolvePagingParams
  },
  data: {
    pageTitle: 'runtimeBundle.title',
    defaultSort: 'id,asc',
    runtimeBundle: 'api-vessel-rb'
  }
};

