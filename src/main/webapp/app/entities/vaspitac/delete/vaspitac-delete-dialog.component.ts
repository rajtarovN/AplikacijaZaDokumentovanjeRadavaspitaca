import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVaspitac } from '../vaspitac.model';
import { VaspitacService } from '../service/vaspitac.service';

@Component({
  templateUrl: './vaspitac-delete-dialog.component.html',
})
export class VaspitacDeleteDialogComponent {
  vaspitac?: IVaspitac;

  constructor(protected vaspitacService: VaspitacService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vaspitacService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
