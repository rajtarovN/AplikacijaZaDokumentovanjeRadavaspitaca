import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PodaciORoditeljimaComponent } from './list/podaci-o-roditeljima.component';
import { PodaciORoditeljimaDetailComponent } from './detail/podaci-o-roditeljima-detail.component';
import { PodaciORoditeljimaUpdateComponent } from './update/podaci-o-roditeljima-update.component';
import { PodaciORoditeljimaDeleteDialogComponent } from './delete/podaci-o-roditeljima-delete-dialog.component';
import { PodaciORoditeljimaRoutingModule } from './route/podaci-o-roditeljima-routing.module';

@NgModule({
  imports: [SharedModule, PodaciORoditeljimaRoutingModule],
  declarations: [
    PodaciORoditeljimaComponent,
    PodaciORoditeljimaDetailComponent,
    PodaciORoditeljimaUpdateComponent,
    PodaciORoditeljimaDeleteDialogComponent,
  ],
  entryComponents: [PodaciORoditeljimaDeleteDialogComponent],
})
export class PodaciORoditeljimaModule {}
