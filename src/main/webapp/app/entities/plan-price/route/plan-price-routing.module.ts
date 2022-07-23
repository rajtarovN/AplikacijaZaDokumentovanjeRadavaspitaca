import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlanPriceComponent } from '../list/plan-price.component';
import { PlanPriceDetailComponent } from '../detail/plan-price-detail.component';
import { PlanPriceUpdateComponent } from '../update/plan-price-update.component';
import { PlanPriceRoutingResolveService } from './plan-price-routing-resolve.service';
import { PisanjePlanaComponent } from '../pisanje-plana/pisanje-plana.component';

const planPriceRoute: Routes = [
  {
    path: '',
    component: PlanPriceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlanPriceDetailComponent,
    resolve: {
      planPrice: PlanPriceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlanPriceUpdateComponent,
    resolve: {
      planPrice: PlanPriceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlanPriceUpdateComponent,
    resolve: {
      planPrice: PlanPriceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'pisanje-plana',
    component: PisanjePlanaComponent,
    resolve: {
      planPrice: PlanPriceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(planPriceRoute)],
  exports: [RouterModule],
})
export class PlanPriceRoutingModule {}
