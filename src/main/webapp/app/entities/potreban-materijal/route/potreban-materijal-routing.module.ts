import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PotrebanMaterijalComponent } from '../list/potreban-materijal.component';
import { PotrebanMaterijalDetailComponent } from '../detail/potreban-materijal-detail.component';
import { PotrebanMaterijalUpdateComponent } from '../update/potreban-materijal-update.component';
import { PotrebanMaterijalRoutingResolveService } from './potreban-materijal-routing-resolve.service';

const potrebanMaterijalRoute: Routes = [
  {
    path: '',
    component: PotrebanMaterijalComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PotrebanMaterijalDetailComponent,
    resolve: {
      potrebanMaterijal: PotrebanMaterijalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PotrebanMaterijalUpdateComponent,
    resolve: {
      potrebanMaterijal: PotrebanMaterijalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PotrebanMaterijalUpdateComponent,
    resolve: {
      potrebanMaterijal: PotrebanMaterijalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(potrebanMaterijalRoute)],
  exports: [RouterModule],
})
export class PotrebanMaterijalRoutingModule {}
