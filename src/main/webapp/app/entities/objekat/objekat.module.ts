import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ObjekatComponent } from './list/objekat.component';
import { ObjekatDetailComponent } from './detail/objekat-detail.component';
import { ObjekatUpdateComponent } from './update/objekat-update.component';
import { ObjekatDeleteDialogComponent } from './delete/objekat-delete-dialog.component';
import { ObjekatRoutingModule } from './route/objekat-routing.module';

@NgModule({
  imports: [SharedModule, ObjekatRoutingModule],
  declarations: [ObjekatComponent, ObjekatDetailComponent, ObjekatUpdateComponent, ObjekatDeleteDialogComponent],
  entryComponents: [ObjekatDeleteDialogComponent],
})
export class ObjekatModule {}
