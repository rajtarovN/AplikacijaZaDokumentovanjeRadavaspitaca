import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AdresaComponent } from './list/adresa.component';
import { AdresaDetailComponent } from './detail/adresa-detail.component';
import { AdresaUpdateComponent } from './update/adresa-update.component';
import { AdresaDeleteDialogComponent } from './delete/adresa-delete-dialog.component';
import { AdresaRoutingModule } from './route/adresa-routing.module';

@NgModule({
  imports: [SharedModule, AdresaRoutingModule],
  declarations: [AdresaComponent, AdresaDetailComponent, AdresaUpdateComponent, AdresaDeleteDialogComponent],
  entryComponents: [AdresaDeleteDialogComponent],
})
export class AdresaModule {}
