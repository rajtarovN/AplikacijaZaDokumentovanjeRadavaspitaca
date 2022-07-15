import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { KomentarNaPricuComponent } from '../list/komentar-na-pricu.component';
import { KomentarNaPricuDetailComponent } from '../detail/komentar-na-pricu-detail.component';
import { KomentarNaPricuUpdateComponent } from '../update/komentar-na-pricu-update.component';
import { KomentarNaPricuRoutingResolveService } from './komentar-na-pricu-routing-resolve.service';

const komentarNaPricuRoute: Routes = [
  {
    path: '',
    component: KomentarNaPricuComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KomentarNaPricuDetailComponent,
    resolve: {
      komentarNaPricu: KomentarNaPricuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KomentarNaPricuUpdateComponent,
    resolve: {
      komentarNaPricu: KomentarNaPricuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KomentarNaPricuUpdateComponent,
    resolve: {
      komentarNaPricu: KomentarNaPricuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(komentarNaPricuRoute)],
  exports: [RouterModule],
})
export class KomentarNaPricuRoutingModule {}
