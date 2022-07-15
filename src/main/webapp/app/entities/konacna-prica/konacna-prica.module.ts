import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { KonacnaPricaComponent } from './list/konacna-prica.component';
import { KonacnaPricaDetailComponent } from './detail/konacna-prica-detail.component';
import { KonacnaPricaUpdateComponent } from './update/konacna-prica-update.component';
import { KonacnaPricaDeleteDialogComponent } from './delete/konacna-prica-delete-dialog.component';
import { KonacnaPricaRoutingModule } from './route/konacna-prica-routing.module';

@NgModule({
  imports: [SharedModule, KonacnaPricaRoutingModule],
  declarations: [KonacnaPricaComponent, KonacnaPricaDetailComponent, KonacnaPricaUpdateComponent, KonacnaPricaDeleteDialogComponent],
  entryComponents: [KonacnaPricaDeleteDialogComponent],
})
export class KonacnaPricaModule {}
