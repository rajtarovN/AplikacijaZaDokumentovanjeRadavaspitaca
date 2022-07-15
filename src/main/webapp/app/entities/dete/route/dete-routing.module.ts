import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeteComponent } from '../list/dete.component';
import { DeteDetailComponent } from '../detail/dete-detail.component';
import { DeteUpdateComponent } from '../update/dete-update.component';
import { DeteRoutingResolveService } from './dete-routing-resolve.service';

const deteRoute: Routes = [
  {
    path: '',
    component: DeteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeteDetailComponent,
    resolve: {
      dete: DeteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeteUpdateComponent,
    resolve: {
      dete: DeteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeteUpdateComponent,
    resolve: {
      dete: DeteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deteRoute)],
  exports: [RouterModule],
})
export class DeteRoutingModule {}
