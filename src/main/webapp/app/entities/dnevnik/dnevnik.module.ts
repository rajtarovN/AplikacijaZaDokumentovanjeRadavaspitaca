import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DnevnikComponent } from './list/dnevnik.component';
import { DnevnikDetailComponent } from './detail/dnevnik-detail.component';
import { DnevnikUpdateComponent } from './update/dnevnik-update.component';
import { DnevnikDeleteDialogComponent } from './delete/dnevnik-delete-dialog.component';
import { DnevnikRoutingModule } from './route/dnevnik-routing.module';

@NgModule({
  imports: [SharedModule, DnevnikRoutingModule],
  declarations: [DnevnikComponent, DnevnikDetailComponent, DnevnikUpdateComponent, DnevnikDeleteDialogComponent],
  entryComponents: [DnevnikDeleteDialogComponent],
})
export class DnevnikModule {}
