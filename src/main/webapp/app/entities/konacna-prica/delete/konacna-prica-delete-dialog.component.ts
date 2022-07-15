import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IKonacnaPrica } from '../konacna-prica.model';
import { KonacnaPricaService } from '../service/konacna-prica.service';

@Component({
  templateUrl: './konacna-prica-delete-dialog.component.html',
})
export class KonacnaPricaDeleteDialogComponent {
  konacnaPrica?: IKonacnaPrica;

  constructor(protected konacnaPricaService: KonacnaPricaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.konacnaPricaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
