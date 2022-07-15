import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PotrebanMaterijalComponent } from './list/potreban-materijal.component';
import { PotrebanMaterijalDetailComponent } from './detail/potreban-materijal-detail.component';
import { PotrebanMaterijalUpdateComponent } from './update/potreban-materijal-update.component';
import { PotrebanMaterijalDeleteDialogComponent } from './delete/potreban-materijal-delete-dialog.component';
import { PotrebanMaterijalRoutingModule } from './route/potreban-materijal-routing.module';

@NgModule({
  imports: [SharedModule, PotrebanMaterijalRoutingModule],
  declarations: [
    PotrebanMaterijalComponent,
    PotrebanMaterijalDetailComponent,
    PotrebanMaterijalUpdateComponent,
    PotrebanMaterijalDeleteDialogComponent,
  ],
  entryComponents: [PotrebanMaterijalDeleteDialogComponent],
})
export class PotrebanMaterijalModule {}
