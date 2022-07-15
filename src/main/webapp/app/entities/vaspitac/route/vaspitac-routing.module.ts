import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VaspitacComponent } from '../list/vaspitac.component';
import { VaspitacDetailComponent } from '../detail/vaspitac-detail.component';
import { VaspitacUpdateComponent } from '../update/vaspitac-update.component';
import { VaspitacRoutingResolveService } from './vaspitac-routing-resolve.service';

const vaspitacRoute: Routes = [
  {
    path: '',
    component: VaspitacComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VaspitacDetailComponent,
    resolve: {
      vaspitac: VaspitacRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VaspitacUpdateComponent,
    resolve: {
      vaspitac: VaspitacRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VaspitacUpdateComponent,
    resolve: {
      vaspitac: VaspitacRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vaspitacRoute)],
  exports: [RouterModule],
})
export class VaspitacRoutingModule {}
