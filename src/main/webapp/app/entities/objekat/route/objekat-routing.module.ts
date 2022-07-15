import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ObjekatComponent } from '../list/objekat.component';
import { ObjekatDetailComponent } from '../detail/objekat-detail.component';
import { ObjekatUpdateComponent } from '../update/objekat-update.component';
import { ObjekatRoutingResolveService } from './objekat-routing-resolve.service';

const objekatRoute: Routes = [
  {
    path: '',
    component: ObjekatComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ObjekatDetailComponent,
    resolve: {
      objekat: ObjekatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ObjekatUpdateComponent,
    resolve: {
      objekat: ObjekatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ObjekatUpdateComponent,
    resolve: {
      objekat: ObjekatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(objekatRoute)],
  exports: [RouterModule],
})
export class ObjekatRoutingModule {}
