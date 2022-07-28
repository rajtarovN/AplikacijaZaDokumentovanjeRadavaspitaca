import { Route } from '@angular/router';
import { RasporedDeceComponent } from './raspored-dece/raspored-dece.component';

export const RASPORED_DECE_ROUTE: Route = {
  path: '',
  component: RasporedDeceComponent,
  data: {
    pageTitle: 'raspored-dece.title',
  },
};
