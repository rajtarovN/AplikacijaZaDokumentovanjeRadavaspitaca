import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ZapazanjeUVeziDetetaComponent } from './list/zapazanje-u-vezi-deteta.component';
import { ZapazanjeUVeziDetetaDetailComponent } from './detail/zapazanje-u-vezi-deteta-detail.component';
import { ZapazanjeUVeziDetetaUpdateComponent } from './update/zapazanje-u-vezi-deteta-update.component';
import { ZapazanjeUVeziDetetaDeleteDialogComponent } from './delete/zapazanje-u-vezi-deteta-delete-dialog.component';
import { ZapazanjeUVeziDetetaRoutingModule } from './route/zapazanje-u-vezi-deteta-routing.module';
import { PregledZapazanjaComponent } from './pregled-zapazanja/pregled-zapazanja/pregled-zapazanja.component';

@NgModule({
  imports: [SharedModule, ZapazanjeUVeziDetetaRoutingModule],
  declarations: [
    ZapazanjeUVeziDetetaComponent,
    ZapazanjeUVeziDetetaDetailComponent,
    ZapazanjeUVeziDetetaUpdateComponent,
    ZapazanjeUVeziDetetaDeleteDialogComponent,
    PregledZapazanjaComponent,
  ],
  entryComponents: [ZapazanjeUVeziDetetaDeleteDialogComponent],
})
export class ZapazanjeUVeziDetetaModule {}
