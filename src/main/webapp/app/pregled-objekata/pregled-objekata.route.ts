import { Route } from '@angular/router';

import { PregledObjekataComponent } from './pregled-objekata.component';

export const PREGLED_OBJEKATA_ROUTE: Route = {
  path: '',
  component: PregledObjekataComponent,
  data: {
    pageTitle: 'pregled-objekata.title',
  },
};
