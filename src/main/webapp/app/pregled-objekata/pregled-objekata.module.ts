import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { PREGLED_OBJEKATA_ROUTE } from './pregled-objekata.route';
import { PregledObjekataComponent } from './pregled-objekata.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([PREGLED_OBJEKATA_ROUTE])],
  declarations: [PregledObjekataComponent],
})
export class PregledObjekataModule {}
