import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { KorisnikComponent } from './list/korisnik.component';
import { KorisnikDetailComponent } from './detail/korisnik-detail.component';
import { KorisnikUpdateComponent } from './update/korisnik-update.component';
import { KorisnikDeleteDialogComponent } from './delete/korisnik-delete-dialog.component';
import { KorisnikRoutingModule } from './route/korisnik-routing.module';

@NgModule({
  imports: [SharedModule, KorisnikRoutingModule],
  declarations: [KorisnikComponent, KorisnikDetailComponent, KorisnikUpdateComponent, KorisnikDeleteDialogComponent],
  entryComponents: [KorisnikDeleteDialogComponent],
})
export class KorisnikModule {}
