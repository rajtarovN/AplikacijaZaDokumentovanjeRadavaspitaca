import { Component, OnInit } from '@angular/core';
import { INeDolasci, NeDolasciDTO } from '../ne-dolasci.model';
import { NeDolasciService } from '../service/ne-dolasci.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-dodaj-razlog',
  templateUrl: './dodaj-razlog.component.html',
  styleUrls: ['./dodaj-razlog.component.scss'],
})
export class DodajRazlogComponent {
  neDolasci?: NeDolasciDTO;
  editForm = this.fb.group({
    razlog: [],
  });

  constructor(protected fb: FormBuilder, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  save(): void {
    //this.neDolasci.razlog = this.editForm.get(['razlog'])!.value;
    this.activeModal.close(this.editForm.get(['razlog'])!.value);
  }
}
