import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeteComponent } from './list/dete.component';
import { DeteDetailComponent } from './detail/dete-detail.component';
import { DeteUpdateComponent } from './update/dete-update.component';
import { DeteDeleteDialogComponent } from './delete/dete-delete-dialog.component';
import { DeteRoutingModule } from './route/dete-routing.module';

@NgModule({
  imports: [SharedModule, DeteRoutingModule],
  declarations: [DeteComponent, DeteDetailComponent, DeteUpdateComponent, DeteDeleteDialogComponent],
  entryComponents: [DeteDeleteDialogComponent],
})
export class DeteModule {}
