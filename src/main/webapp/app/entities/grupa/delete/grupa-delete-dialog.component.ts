import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGrupa } from '../grupa.model';
import { GrupaService } from '../service/grupa.service';

@Component({
  templateUrl: './grupa-delete-dialog.component.html',
})
export class GrupaDeleteDialogComponent {
  grupa?: IGrupa;

  constructor(protected grupaService: GrupaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.grupaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
