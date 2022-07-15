import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { KorisnikComponent } from '../list/korisnik.component';
import { KorisnikDetailComponent } from '../detail/korisnik-detail.component';
import { KorisnikUpdateComponent } from '../update/korisnik-update.component';
import { KorisnikRoutingResolveService } from './korisnik-routing-resolve.service';

const korisnikRoute: Routes = [
  {
    path: '',
    component: KorisnikComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KorisnikDetailComponent,
    resolve: {
      korisnik: KorisnikRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KorisnikUpdateComponent,
    resolve: {
      korisnik: KorisnikRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KorisnikUpdateComponent,
    resolve: {
      korisnik: KorisnikRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(korisnikRoute)],
  exports: [RouterModule],
})
export class KorisnikRoutingModule {}
