import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RoditeljComponent } from '../list/roditelj.component';
import { RoditeljDetailComponent } from '../detail/roditelj-detail.component';
import { RoditeljUpdateComponent } from '../update/roditelj-update.component';
import { RoditeljRoutingResolveService } from './roditelj-routing-resolve.service';

const roditeljRoute: Routes = [
  {
    path: '',
    component: RoditeljComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoditeljDetailComponent,
    resolve: {
      roditelj: RoditeljRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoditeljUpdateComponent,
    resolve: {
      roditelj: RoditeljRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoditeljUpdateComponent,
    resolve: {
      roditelj: RoditeljRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(roditeljRoute)],
  exports: [RouterModule],
})
export class RoditeljRoutingModule {}
