import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPotrebanMaterijal } from '../potreban-materijal.model';
import { PotrebanMaterijalService } from '../service/potreban-materijal.service';

@Component({
  templateUrl: './potreban-materijal-delete-dialog.component.html',
})
export class PotrebanMaterijalDeleteDialogComponent {
  potrebanMaterijal?: IPotrebanMaterijal;

  constructor(protected potrebanMaterijalService: PotrebanMaterijalService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.potrebanMaterijalService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
