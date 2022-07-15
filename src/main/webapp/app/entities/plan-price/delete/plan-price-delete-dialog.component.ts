import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlanPrice } from '../plan-price.model';
import { PlanPriceService } from '../service/plan-price.service';

@Component({
  templateUrl: './plan-price-delete-dialog.component.html',
})
export class PlanPriceDeleteDialogComponent {
  planPrice?: IPlanPrice;

  constructor(protected planPriceService: PlanPriceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.planPriceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
