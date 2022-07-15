import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDirektor } from '../direktor.model';
import { DirektorService } from '../service/direktor.service';

@Component({
  templateUrl: './direktor-delete-dialog.component.html',
})
export class DirektorDeleteDialogComponent {
  direktor?: IDirektor;

  constructor(protected direktorService: DirektorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.direktorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
