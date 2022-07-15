import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DirektorComponent } from '../list/direktor.component';
import { DirektorDetailComponent } from '../detail/direktor-detail.component';
import { DirektorUpdateComponent } from '../update/direktor-update.component';
import { DirektorRoutingResolveService } from './direktor-routing-resolve.service';

const direktorRoute: Routes = [
  {
    path: '',
    component: DirektorComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DirektorDetailComponent,
    resolve: {
      direktor: DirektorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DirektorUpdateComponent,
    resolve: {
      direktor: DirektorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DirektorUpdateComponent,
    resolve: {
      direktor: DirektorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(direktorRoute)],
  exports: [RouterModule],
})
export class DirektorRoutingModule {}
