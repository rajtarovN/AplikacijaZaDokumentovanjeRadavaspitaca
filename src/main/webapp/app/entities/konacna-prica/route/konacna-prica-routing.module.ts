import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { KonacnaPricaComponent } from '../list/konacna-prica.component';
import { KonacnaPricaDetailComponent } from '../detail/konacna-prica-detail.component';
import { KonacnaPricaUpdateComponent } from '../update/konacna-prica-update.component';
import { KonacnaPricaRoutingResolveService } from './konacna-prica-routing-resolve.service';

const konacnaPricaRoute: Routes = [
  {
    path: '',
    component: KonacnaPricaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KonacnaPricaDetailComponent,
    resolve: {
      konacnaPrica: KonacnaPricaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KonacnaPricaUpdateComponent,
    resolve: {
      konacnaPrica: KonacnaPricaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KonacnaPricaUpdateComponent,
    resolve: {
      konacnaPrica: KonacnaPricaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(konacnaPricaRoute)],
  exports: [RouterModule],
})
export class KonacnaPricaRoutingModule {}
