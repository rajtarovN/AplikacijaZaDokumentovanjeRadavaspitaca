import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { PREGLED_VASPITACA_ROUTE } from './pregled-vaspitaca.route';
import { PregledVaspitacaComponent } from './pregled-vaspitaca.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([PREGLED_VASPITACA_ROUTE])],
  declarations: [PregledVaspitacaComponent],
})
export class PregledVaspitacaModule {}
