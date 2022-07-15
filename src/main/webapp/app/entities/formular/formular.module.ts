import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FormularComponent } from './list/formular.component';
import { FormularDetailComponent } from './detail/formular-detail.component';
import { FormularUpdateComponent } from './update/formular-update.component';
import { FormularDeleteDialogComponent } from './delete/formular-delete-dialog.component';
import { FormularRoutingModule } from './route/formular-routing.module';

@NgModule({
  imports: [SharedModule, FormularRoutingModule],
  declarations: [FormularComponent, FormularDetailComponent, FormularUpdateComponent, FormularDeleteDialogComponent],
  entryComponents: [FormularDeleteDialogComponent],
})
export class FormularModule {}
