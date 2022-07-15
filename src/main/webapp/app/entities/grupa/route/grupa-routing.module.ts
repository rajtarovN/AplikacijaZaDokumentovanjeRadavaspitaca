import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GrupaComponent } from '../list/grupa.component';
import { GrupaDetailComponent } from '../detail/grupa-detail.component';
import { GrupaUpdateComponent } from '../update/grupa-update.component';
import { GrupaRoutingResolveService } from './grupa-routing-resolve.service';

const grupaRoute: Routes = [
  {
    path: '',
    component: GrupaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GrupaDetailComponent,
    resolve: {
      grupa: GrupaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GrupaUpdateComponent,
    resolve: {
      grupa: GrupaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GrupaUpdateComponent,
    resolve: {
      grupa: GrupaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(grupaRoute)],
  exports: [RouterModule],
})
export class GrupaRoutingModule {}
