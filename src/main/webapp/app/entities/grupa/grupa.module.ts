import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GrupaComponent } from './list/grupa.component';
import { GrupaDetailComponent } from './detail/grupa-detail.component';
import { GrupaUpdateComponent } from './update/grupa-update.component';
import { GrupaDeleteDialogComponent } from './delete/grupa-delete-dialog.component';
import { GrupaRoutingModule } from './route/grupa-routing.module';

@NgModule({
  imports: [SharedModule, GrupaRoutingModule],
  declarations: [GrupaComponent, GrupaDetailComponent, GrupaUpdateComponent, GrupaDeleteDialogComponent],
  entryComponents: [GrupaDeleteDialogComponent],
})
export class GrupaModule {}
