import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RoditeljComponent } from './list/roditelj.component';
import { RoditeljDetailComponent } from './detail/roditelj-detail.component';
import { RoditeljUpdateComponent } from './update/roditelj-update.component';
import { RoditeljDeleteDialogComponent } from './delete/roditelj-delete-dialog.component';
import { RoditeljRoutingModule } from './route/roditelj-routing.module';

@NgModule({
  imports: [SharedModule, RoditeljRoutingModule],
  declarations: [RoditeljComponent, RoditeljDetailComponent, RoditeljUpdateComponent, RoditeljDeleteDialogComponent],
  entryComponents: [RoditeljDeleteDialogComponent],
})
export class RoditeljModule {}
