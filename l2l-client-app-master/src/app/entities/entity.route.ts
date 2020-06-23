import { Routes } from '@angular/router';

import {
    processInstanceRoute,
    processDefinitionRoutes,
    taskRoutes

} from './';

const ENTITY_ROUTES = [
  processInstanceRoute,
  ...taskRoutes,
  ...processDefinitionRoutes
];

export const entityState: Routes = [
    {
        path: '',
        children: ENTITY_ROUTES
    }
];
