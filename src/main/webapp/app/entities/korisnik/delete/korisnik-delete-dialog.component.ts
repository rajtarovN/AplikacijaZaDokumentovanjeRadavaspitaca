import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IKorisnik } from '../korisnik.model';
import { KorisnikService } from '../service/korisnik.service';

@Component({
  templateUrl: './korisnik-delete-dialog.component.html',
})
export class KorisnikDeleteDialogComponent {
  korisnik?: IKorisnik;

  constructor(protected korisnikService: KorisnikService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.korisnikService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
