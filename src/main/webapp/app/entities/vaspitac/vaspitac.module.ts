import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VaspitacComponent } from './list/vaspitac.component';
import { VaspitacDetailComponent } from './detail/vaspitac-detail.component';
import { VaspitacUpdateComponent } from './update/vaspitac-update.component';
import { VaspitacDeleteDialogComponent } from './delete/vaspitac-delete-dialog.component';
import { VaspitacRoutingModule } from './route/vaspitac-routing.module';

@NgModule({
  imports: [SharedModule, VaspitacRoutingModule],
  declarations: [VaspitacComponent, VaspitacDetailComponent, VaspitacUpdateComponent, VaspitacDeleteDialogComponent],
  entryComponents: [VaspitacDeleteDialogComponent],
})
export class VaspitacModule {}
