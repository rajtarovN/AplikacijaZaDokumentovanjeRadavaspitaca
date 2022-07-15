import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormularComponent } from '../list/formular.component';
import { FormularDetailComponent } from '../detail/formular-detail.component';
import { FormularUpdateComponent } from '../update/formular-update.component';
import { FormularRoutingResolveService } from './formular-routing-resolve.service';

const formularRoute: Routes = [
  {
    path: '',
    component: FormularComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormularDetailComponent,
    resolve: {
      formular: FormularRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormularUpdateComponent,
    resolve: {
      formular: FormularRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormularUpdateComponent,
    resolve: {
      formular: FormularRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formularRoute)],
  exports: [RouterModule],
})
export class FormularRoutingModule {}
