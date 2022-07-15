import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AdresaComponent } from '../list/adresa.component';
import { AdresaDetailComponent } from '../detail/adresa-detail.component';
import { AdresaUpdateComponent } from '../update/adresa-update.component';
import { AdresaRoutingResolveService } from './adresa-routing-resolve.service';

const adresaRoute: Routes = [
  {
    path: '',
    component: AdresaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AdresaDetailComponent,
    resolve: {
      adresa: AdresaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AdresaUpdateComponent,
    resolve: {
      adresa: AdresaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AdresaUpdateComponent,
    resolve: {
      adresa: AdresaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(adresaRoute)],
  exports: [RouterModule],
})
export class AdresaRoutingModule {}
