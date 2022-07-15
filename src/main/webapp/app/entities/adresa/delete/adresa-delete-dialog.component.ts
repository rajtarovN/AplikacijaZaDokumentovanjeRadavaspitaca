import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdresa } from '../adresa.model';
import { AdresaService } from '../service/adresa.service';

@Component({
  templateUrl: './adresa-delete-dialog.component.html',
})
export class AdresaDeleteDialogComponent {
  adresa?: IAdresa;

  constructor(protected adresaService: AdresaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.adresaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
