import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDete } from '../dete.model';
import { DeteService } from '../service/dete.service';

@Component({
  templateUrl: './dete-delete-dialog.component.html',
})
export class DeteDeleteDialogComponent {
  dete?: IDete;

  constructor(protected deteService: DeteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
