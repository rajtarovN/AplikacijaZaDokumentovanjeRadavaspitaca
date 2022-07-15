import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IZapazanjeUVeziDeteta } from '../zapazanje-u-vezi-deteta.model';
import { ZapazanjeUVeziDetetaService } from '../service/zapazanje-u-vezi-deteta.service';

@Component({
  templateUrl: './zapazanje-u-vezi-deteta-delete-dialog.component.html',
})
export class ZapazanjeUVeziDetetaDeleteDialogComponent {
  zapazanjeUVeziDeteta?: IZapazanjeUVeziDeteta;

  constructor(protected zapazanjeUVeziDetetaService: ZapazanjeUVeziDetetaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.zapazanjeUVeziDetetaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
