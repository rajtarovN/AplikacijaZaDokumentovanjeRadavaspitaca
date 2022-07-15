import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IKonacnaPrica, KonacnaPrica } from '../konacna-prica.model';
import { KonacnaPricaService } from '../service/konacna-prica.service';

@Component({
  selector: 'jhi-konacna-prica-update',
  templateUrl: './konacna-prica-update.component.html',
})
export class KonacnaPricaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    tekst: [],
  });

  constructor(protected konacnaPricaService: KonacnaPricaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ konacnaPrica }) => {
      this.updateForm(konacnaPrica);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const konacnaPrica = this.createFromForm();
    if (konacnaPrica.id !== undefined) {
      this.subscribeToSaveResponse(this.konacnaPricaService.update(konacnaPrica));
    } else {
      this.subscribeToSaveResponse(this.konacnaPricaService.create(konacnaPrica));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKonacnaPrica>>): void {
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

  protected updateForm(konacnaPrica: IKonacnaPrica): void {
    this.editForm.patchValue({
      id: konacnaPrica.id,
      tekst: konacnaPrica.tekst,
    });
  }

  protected createFromForm(): IKonacnaPrica {
    return {
      ...new KonacnaPrica(),
      id: this.editForm.get(['id'])!.value,
      tekst: this.editForm.get(['tekst'])!.value,
    };
  }
}
