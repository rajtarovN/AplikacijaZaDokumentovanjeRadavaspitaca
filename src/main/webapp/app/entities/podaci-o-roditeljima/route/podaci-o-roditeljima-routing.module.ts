import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PodaciORoditeljimaComponent } from '../list/podaci-o-roditeljima.component';
import { PodaciORoditeljimaDetailComponent } from '../detail/podaci-o-roditeljima-detail.component';
import { PodaciORoditeljimaUpdateComponent } from '../update/podaci-o-roditeljima-update.component';
import { PodaciORoditeljimaRoutingResolveService } from './podaci-o-roditeljima-routing-resolve.service';

const podaciORoditeljimaRoute: Routes = [
  {
    path: '',
    component: PodaciORoditeljimaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PodaciORoditeljimaDetailComponent,
    resolve: {
      podaciORoditeljima: PodaciORoditeljimaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PodaciORoditeljimaUpdateComponent,
    resolve: {
      podaciORoditeljima: PodaciORoditeljimaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PodaciORoditeljimaUpdateComponent,
    resolve: {
      podaciORoditeljima: PodaciORoditeljimaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(podaciORoditeljimaRoute)],
  exports: [RouterModule],
})
export class PodaciORoditeljimaRoutingModule {}
