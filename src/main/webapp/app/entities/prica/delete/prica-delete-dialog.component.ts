import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrica } from '../prica.model';
import { PricaService } from '../service/prica.service';

@Component({
  templateUrl: './prica-delete-dialog.component.html',
})
export class PricaDeleteDialogComponent {
  prica?: IPrica;

  constructor(protected pricaService: PricaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pricaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
