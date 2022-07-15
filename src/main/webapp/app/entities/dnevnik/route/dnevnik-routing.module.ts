import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DnevnikComponent } from '../list/dnevnik.component';
import { DnevnikDetailComponent } from '../detail/dnevnik-detail.component';
import { DnevnikUpdateComponent } from '../update/dnevnik-update.component';
import { DnevnikRoutingResolveService } from './dnevnik-routing-resolve.service';

const dnevnikRoute: Routes = [
  {
    path: '',
    component: DnevnikComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DnevnikDetailComponent,
    resolve: {
      dnevnik: DnevnikRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DnevnikUpdateComponent,
    resolve: {
      dnevnik: DnevnikRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DnevnikUpdateComponent,
    resolve: {
      dnevnik: DnevnikRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dnevnikRoute)],
  exports: [RouterModule],
})
export class DnevnikRoutingModule {}
