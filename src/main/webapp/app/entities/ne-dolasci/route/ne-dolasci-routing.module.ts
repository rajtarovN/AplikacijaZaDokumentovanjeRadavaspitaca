import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NeDolasciComponent } from '../list/ne-dolasci.component';
import { NeDolasciDetailComponent } from '../detail/ne-dolasci-detail.component';
import { NeDolasciUpdateComponent } from '../update/ne-dolasci-update.component';
import { NeDolasciRoutingResolveService } from './ne-dolasci-routing-resolve.service';
import { IzostanciComponent } from '../izostanci/izostanci.component';

const neDolasciRoute: Routes = [
  {
    path: '',
    component: NeDolasciComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NeDolasciDetailComponent,
    resolve: {
      neDolasci: NeDolasciRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NeDolasciUpdateComponent,
    resolve: {
      neDolasci: NeDolasciRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NeDolasciUpdateComponent,
    resolve: {
      neDolasci: NeDolasciRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'izostanci',
    component: IzostanciComponent,
    resolve: {
      neDolasci: NeDolasciRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(neDolasciRoute)],
  exports: [RouterModule],
})
export class NeDolasciRoutingModule {}
