import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../shared';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */
import { ProcessInstanceComponent } from './process-instance/process-instance.component';
import { entityState } from './entity.route';
import { ProcessInstanceService } from './process-instance/process-instance.service';
import { ProcessDefinitionComponent } from './process-definition/process-definition.component';
import { TaskComponent } from './task/task.component';
import { ProcessDefinitionService } from './process-definition/process-definition.service';
import { TaskService } from './task/task.service';
import { ProcessStartComponent } from './process-definition/process-start.component';
import {FormService} from './form/form.service';
import {FormComponent} from './form/form.component';
import {TaskCompleteComponent} from './task/task-complete.component';
import {ProcessStartService} from './process-definition/process-start.service';
@NgModule({
    // prettier-ignore
    imports: [
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
      SharedModule, RouterModule.forChild(entityState)
    ],
    declarations: [
      ProcessInstanceComponent,
      ProcessDefinitionComponent,
      TaskComponent,
      ProcessStartComponent,
      FormComponent,
      TaskCompleteComponent
    ],
    entryComponents: [
      ProcessInstanceComponent,
      ProcessDefinitionComponent,
      TaskComponent,
      ProcessStartComponent,
      FormComponent,
      TaskCompleteComponent
    ],
    providers: [
      ProcessInstanceService,
      ProcessDefinitionService,
      TaskService,
      FormService,
      ProcessStartService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EntityModule {

}
