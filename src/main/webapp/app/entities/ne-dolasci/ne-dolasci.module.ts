import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NeDolasciComponent } from './list/ne-dolasci.component';
import { NeDolasciDetailComponent } from './detail/ne-dolasci-detail.component';
import { NeDolasciUpdateComponent } from './update/ne-dolasci-update.component';
import { NeDolasciDeleteDialogComponent } from './delete/ne-dolasci-delete-dialog.component';
import { NeDolasciRoutingModule } from './route/ne-dolasci-routing.module';

@NgModule({
  imports: [SharedModule, NeDolasciRoutingModule],
  declarations: [NeDolasciComponent, NeDolasciDetailComponent, NeDolasciUpdateComponent, NeDolasciDeleteDialogComponent],
  entryComponents: [NeDolasciDeleteDialogComponent],
})
export class NeDolasciModule {}
