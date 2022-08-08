import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormular } from '../formular.model';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { FormularService } from '../service/formular.service';
import { StatusFormulara } from '../../enumerations/status-formulara.model';
import { IPodaciORoditeljima } from '../../podaci-o-roditeljima/podaci-o-roditeljima.model';

@Component({
  selector: 'jhi-formular-detail',
  templateUrl: './formular-detail.component.html',
})
export class FormularDetailComponent implements OnInit {
  isSaving: false;
  formular: IFormular | null = null;
  status: StatusFormulara.RASPOREDJEN;
  podaciORoditeljima1: IPodaciORoditeljima | null = null;
  podaciORoditeljima2: IPodaciORoditeljima | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected formularService: FormularService) {
    this.isSaving = false;
    this.status = StatusFormulara.RASPOREDJEN;
  }

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ formular }) => {
      this.formular = formular;
      if (this.formular?.podaciORoditeljimas != null) {
        this.podaciORoditeljima1 = this.formular.podaciORoditeljimas[0];
        if (this.formular.podaciORoditeljimas.length > 1) {
          this.podaciORoditeljima2 = this.formular.podaciORoditeljimas[1];
        }
      }
      // eslint-disable-next-line no-console
      console.log(formular);
    });
  }

  previousState(): void {
    window.history.back();
  }
  odobri(): void {
    this.subscribeToSaveResponse(this.formularService.odobri(this.formular!.id!));
  }
  odbij(): void {
    this.subscribeToSaveResponse(this.formularService.odbij(this.formular!.id!));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormular>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
}
