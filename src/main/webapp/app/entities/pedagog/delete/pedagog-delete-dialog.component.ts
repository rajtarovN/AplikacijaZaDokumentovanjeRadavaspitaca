import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPedagog } from '../pedagog.model';
import { PedagogService } from '../service/pedagog.service';

@Component({
  templateUrl: './pedagog-delete-dialog.component.html',
})
export class PedagogDeleteDialogComponent {
  pedagog?: IPedagog;

  constructor(protected pedagogService: PedagogService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pedagogService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
