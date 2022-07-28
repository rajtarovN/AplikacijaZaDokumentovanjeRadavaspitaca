import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { RASPORED_DECE_ROUTE } from './raspored-dece.route';
import { RasporedDeceComponent } from './raspored-dece/raspored-dece.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([RASPORED_DECE_ROUTE])],
  declarations: [RasporedDeceComponent],
})
export class RasporedDeceModule {}
