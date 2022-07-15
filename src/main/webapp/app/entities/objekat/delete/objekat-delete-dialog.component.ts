import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IObjekat } from '../objekat.model';
import { ObjekatService } from '../service/objekat.service';

@Component({
  templateUrl: './objekat-delete-dialog.component.html',
})
export class ObjekatDeleteDialogComponent {
  objekat?: IObjekat;

  constructor(protected objekatService: ObjekatService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.objekatService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
