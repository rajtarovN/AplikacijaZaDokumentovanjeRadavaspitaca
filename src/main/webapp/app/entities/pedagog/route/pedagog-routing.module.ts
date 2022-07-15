import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PedagogComponent } from '../list/pedagog.component';
import { PedagogDetailComponent } from '../detail/pedagog-detail.component';
import { PedagogUpdateComponent } from '../update/pedagog-update.component';
import { PedagogRoutingResolveService } from './pedagog-routing-resolve.service';

const pedagogRoute: Routes = [
  {
    path: '',
    component: PedagogComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PedagogDetailComponent,
    resolve: {
      pedagog: PedagogRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PedagogUpdateComponent,
    resolve: {
      pedagog: PedagogRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PedagogUpdateComponent,
    resolve: {
      pedagog: PedagogRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pedagogRoute)],
  exports: [RouterModule],
})
export class PedagogRoutingModule {}
