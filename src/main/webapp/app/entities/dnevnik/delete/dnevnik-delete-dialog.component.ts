import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDnevnik } from '../dnevnik.model';
import { DnevnikService } from '../service/dnevnik.service';

@Component({
  templateUrl: './dnevnik-delete-dialog.component.html',
})
export class DnevnikDeleteDialogComponent {
  dnevnik?: IDnevnik;

  constructor(protected dnevnikService: DnevnikService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dnevnikService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
