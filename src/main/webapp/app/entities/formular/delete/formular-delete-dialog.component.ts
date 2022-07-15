import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormular } from '../formular.model';
import { FormularService } from '../service/formular.service';

@Component({
  templateUrl: './formular-delete-dialog.component.html',
})
export class FormularDeleteDialogComponent {
  formular?: IFormular;

  constructor(protected formularService: FormularService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formularService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
