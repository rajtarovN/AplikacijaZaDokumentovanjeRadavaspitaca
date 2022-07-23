import { Route } from '@angular/router';

import { PregledVaspitacaComponent } from './pregled-vaspitaca.component';

export const PREGLED_VASPITACA_ROUTE: Route = {
  path: '',
  component: PregledVaspitacaComponent,
  data: {
    pageTitle: 'pregled-vaspitaca.title',
  },
};
