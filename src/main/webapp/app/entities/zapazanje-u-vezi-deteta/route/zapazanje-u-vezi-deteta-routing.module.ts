import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ZapazanjeUVeziDetetaComponent } from '../list/zapazanje-u-vezi-deteta.component';
import { ZapazanjeUVeziDetetaDetailComponent } from '../detail/zapazanje-u-vezi-deteta-detail.component';
import { ZapazanjeUVeziDetetaUpdateComponent } from '../update/zapazanje-u-vezi-deteta-update.component';
import { ZapazanjeUVeziDetetaRoutingResolveService } from './zapazanje-u-vezi-deteta-routing-resolve.service';
import { PregledZapazanjaComponent } from '../pregled-zapazanja/pregled-zapazanja/pregled-zapazanja.component';

const zapazanjeUVeziDetetaRoute: Routes = [
  {
    path: '',
    component: ZapazanjeUVeziDetetaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ZapazanjeUVeziDetetaDetailComponent,
    resolve: {
      zapazanjeUVeziDeteta: ZapazanjeUVeziDetetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ZapazanjeUVeziDetetaUpdateComponent,
    resolve: {
      zapazanjeUVeziDeteta: ZapazanjeUVeziDetetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ZapazanjeUVeziDetetaUpdateComponent,
    resolve: {
      zapazanjeUVeziDeteta: ZapazanjeUVeziDetetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/pregled',
    component: PregledZapazanjaComponent,
    resolve: {
      zapazanjeUVeziDeteta: ZapazanjeUVeziDetetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(zapazanjeUVeziDetetaRoute)],
  exports: [RouterModule],
})
export class ZapazanjeUVeziDetetaRoutingModule {}
