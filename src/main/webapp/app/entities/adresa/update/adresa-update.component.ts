import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAdresa, Adresa } from '../adresa.model';
import { AdresaService } from '../service/adresa.service';

@Component({
  selector: 'jhi-adresa-update',
  templateUrl: './adresa-update.component.html',
})
export class AdresaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    mesto: [],
    ulica: [],
    broj: [],
    id: [],
  });

  constructor(protected adresaService: AdresaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adresa }) => {
      this.updateForm(adresa);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const adresa = this.createFromForm();
    if (adresa.id !== undefined) {
      this.subscribeToSaveResponse(this.adresaService.update(adresa));
    } else {
      this.subscribeToSaveResponse(this.adresaService.create(adresa));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdresa>>): void {
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

  protected updateForm(adresa: IAdresa): void {
    this.editForm.patchValue({
      mesto: adresa.mesto,
      ulica: adresa.ulica,
      broj: adresa.broj,
      id: adresa.id,
    });
  }

  protected createFromForm(): IAdresa {
    return {
      ...new Adresa(),
      mesto: this.editForm.get(['mesto'])!.value,
      ulica: this.editForm.get(['ulica'])!.value,
      broj: this.editForm.get(['broj'])!.value,
      id: this.editForm.get(['id'])!.value,
    };
  }
}
