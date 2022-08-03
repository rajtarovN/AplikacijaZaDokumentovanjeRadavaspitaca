import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SettingsComponent } from './settings.component';

export const settingsRoute: Route = {
  path: 'settings',
  component: SettingsComponent,
  data: {
    pageTitle: 'global.menu.account.settings',
    authorities: ['ROLE_ADMIN', 'ROLE_VASPITAC', 'ROLE_DIREKTOR', 'ROLE_PEDAGOG', 'ROLE_USER', 'ROLE_RODITELJ'],
  },
  canActivate: [UserRouteAccessService],
};
