import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PedagogComponent } from './list/pedagog.component';
import { PedagogDetailComponent } from './detail/pedagog-detail.component';
import { PedagogUpdateComponent } from './update/pedagog-update.component';
import { PedagogDeleteDialogComponent } from './delete/pedagog-delete-dialog.component';
import { PedagogRoutingModule } from './route/pedagog-routing.module';
import { AccountModule } from '../../account/account.module';

@NgModule({
  imports: [SharedModule, PedagogRoutingModule, AccountModule],
  declarations: [PedagogComponent, PedagogDetailComponent, PedagogUpdateComponent, PedagogDeleteDialogComponent],
  entryComponents: [PedagogDeleteDialogComponent],
})
export class PedagogModule {}
