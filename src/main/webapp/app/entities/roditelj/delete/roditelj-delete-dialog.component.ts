import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoditelj } from '../roditelj.model';
import { RoditeljService } from '../service/roditelj.service';

@Component({
  templateUrl: './roditelj-delete-dialog.component.html',
})
export class RoditeljDeleteDialogComponent {
  roditelj?: IRoditelj;

  constructor(protected roditeljService: RoditeljService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roditeljService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
