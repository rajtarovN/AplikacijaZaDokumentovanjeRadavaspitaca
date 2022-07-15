import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPodaciORoditeljima } from '../podaci-o-roditeljima.model';
import { PodaciORoditeljimaService } from '../service/podaci-o-roditeljima.service';

@Component({
  templateUrl: './podaci-o-roditeljima-delete-dialog.component.html',
})
export class PodaciORoditeljimaDeleteDialogComponent {
  podaciORoditeljima?: IPodaciORoditeljima;

  constructor(protected podaciORoditeljimaService: PodaciORoditeljimaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.podaciORoditeljimaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
