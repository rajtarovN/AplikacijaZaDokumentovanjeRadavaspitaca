import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INeDolasci } from '../ne-dolasci.model';
import { NeDolasciService } from '../service/ne-dolasci.service';

@Component({
  templateUrl: './ne-dolasci-delete-dialog.component.html',
})
export class NeDolasciDeleteDialogComponent {
  neDolasci?: INeDolasci;

  constructor(protected neDolasciService: NeDolasciService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.neDolasciService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
