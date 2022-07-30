import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DirektorComponent } from './list/direktor.component';
import { DirektorDetailComponent } from './detail/direktor-detail.component';
import { DirektorUpdateComponent } from './update/direktor-update.component';
import { DirektorDeleteDialogComponent } from './delete/direktor-delete-dialog.component';
import { DirektorRoutingModule } from './route/direktor-routing.module';
import { AccountModule } from '../../account/account.module';

@NgModule({
  imports: [SharedModule, DirektorRoutingModule, AccountModule],
  declarations: [DirektorComponent, DirektorDetailComponent, DirektorUpdateComponent, DirektorDeleteDialogComponent],
  entryComponents: [DirektorDeleteDialogComponent],
})
export class DirektorModule {}
