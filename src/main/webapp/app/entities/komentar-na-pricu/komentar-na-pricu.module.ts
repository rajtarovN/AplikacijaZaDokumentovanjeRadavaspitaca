import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { KomentarNaPricuComponent } from './list/komentar-na-pricu.component';
import { KomentarNaPricuDetailComponent } from './detail/komentar-na-pricu-detail.component';
import { KomentarNaPricuUpdateComponent } from './update/komentar-na-pricu-update.component';
import { KomentarNaPricuDeleteDialogComponent } from './delete/komentar-na-pricu-delete-dialog.component';
import { KomentarNaPricuRoutingModule } from './route/komentar-na-pricu-routing.module';

@NgModule({
  imports: [SharedModule, KomentarNaPricuRoutingModule],
  declarations: [
    KomentarNaPricuComponent,
    KomentarNaPricuDetailComponent,
    KomentarNaPricuUpdateComponent,
    KomentarNaPricuDeleteDialogComponent,
  ],
  entryComponents: [KomentarNaPricuDeleteDialogComponent],
})
export class KomentarNaPricuModule {}
