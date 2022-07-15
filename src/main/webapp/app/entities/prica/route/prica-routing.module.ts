import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PricaComponent } from '../list/prica.component';
import { PricaDetailComponent } from '../detail/prica-detail.component';
import { PricaUpdateComponent } from '../update/prica-update.component';
import { PricaRoutingResolveService } from './prica-routing-resolve.service';

const pricaRoute: Routes = [
  {
    path: '',
    component: PricaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PricaDetailComponent,
    resolve: {
      prica: PricaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PricaUpdateComponent,
    resolve: {
      prica: PricaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PricaUpdateComponent,
    resolve: {
      prica: PricaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pricaRoute)],
  exports: [RouterModule],
})
export class PricaRoutingModule {}
