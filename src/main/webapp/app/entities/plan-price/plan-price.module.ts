import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PlanPriceComponent } from './list/plan-price.component';
import { PlanPriceDetailComponent } from './detail/plan-price-detail.component';
import { PlanPriceUpdateComponent } from './update/plan-price-update.component';
import { PlanPriceDeleteDialogComponent } from './delete/plan-price-delete-dialog.component';
import { PlanPriceRoutingModule } from './route/plan-price-routing.module';
import { PisanjePlanaComponent } from './pisanje-plana/pisanje-plana.component';

@NgModule({
  imports: [SharedModule, PlanPriceRoutingModule],
  declarations: [
    PlanPriceComponent,
    PlanPriceDetailComponent,
    PlanPriceUpdateComponent,
    PlanPriceDeleteDialogComponent,
    PisanjePlanaComponent,
  ],
  entryComponents: [PlanPriceDeleteDialogComponent],
})
export class PlanPriceModule {}
