import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IKomentarNaPricu } from '../komentar-na-pricu.model';
import { KomentarNaPricuService } from '../service/komentar-na-pricu.service';

@Component({
  templateUrl: './komentar-na-pricu-delete-dialog.component.html',
})
export class KomentarNaPricuDeleteDialogComponent {
  komentarNaPricu?: IKomentarNaPricu;

  constructor(protected komentarNaPricuService: KomentarNaPricuService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.komentarNaPricuService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
