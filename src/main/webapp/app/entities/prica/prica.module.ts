import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PricaComponent } from './list/prica.component';
import { PricaDetailComponent } from './detail/prica-detail.component';
import { PricaUpdateComponent } from './update/prica-update.component';
import { PricaDeleteDialogComponent } from './delete/prica-delete-dialog.component';
import { PricaRoutingModule } from './route/prica-routing.module';

@NgModule({
  imports: [SharedModule, PricaRoutingModule],
  declarations: [PricaComponent, PricaDetailComponent, PricaUpdateComponent, PricaDeleteDialogComponent],
  entryComponents: [PricaDeleteDialogComponent],
})
export class PricaModule {}
