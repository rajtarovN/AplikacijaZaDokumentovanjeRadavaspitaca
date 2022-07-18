import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IObjekat, Objekat } from '../objekat.model';
import { ObjekatService } from '../service/objekat.service';
import { IAdresa } from 'app/entities/adresa/adresa.model';
import { AdresaService } from 'app/entities/adresa/service/adresa.service';

@Component({
  selector: 'jhi-objekat-update',
  templateUrl: './objekat-update.component.html',
})
export class ObjekatUpdateComponent implements OnInit {
  isSaving = false;

  adresasCollection: IAdresa[] = [];

  editForm = this.fb.group({
    opis: [],
    naziv: [],
    id: [],
    adresa: [],
  });

  constructor(
    protected objekatService: ObjekatService,
    protected adresaService: AdresaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ objekat }) => {
      this.updateForm(objekat);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const objekat = this.createFromForm();
    if (objekat.id !== undefined) {
      this.subscribeToSaveResponse(this.objekatService.update(objekat));
    } else {
      this.subscribeToSaveResponse(this.objekatService.create(objekat));
    }
  }

  trackAdresaById(_index: number, item: IAdresa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IObjekat>>): void {
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

  protected updateForm(objekat: IObjekat): void {
    this.editForm.patchValue({
      opis: objekat.opis,
      naziv: objekat.naziv,
      id: objekat.id,
      adresa: objekat.adresa,
    });

    this.adresasCollection = this.adresaService.addAdresaToCollectionIfMissing(this.adresasCollection, objekat.adresa);
  }

  protected loadRelationshipsOptions(): void {
    this.adresaService
      .query({ filter: 'objekat-is-null' })
      .pipe(map((res: HttpResponse<IAdresa[]>) => res.body ?? []))
      .pipe(map((adresas: IAdresa[]) => this.adresaService.addAdresaToCollectionIfMissing(adresas, this.editForm.get('adresa')!.value)))
      .subscribe((adresas: IAdresa[]) => (this.adresasCollection = adresas));
  }

  protected createFromForm(): IObjekat {
    return {
      ...new Objekat(),
      opis: this.editForm.get(['opis'])!.value,
      naziv: this.editForm.get(['naziv'])!.value,
      id: this.editForm.get(['id'])!.value,
      adresa: this.editForm.get(['adresa'])!.value,
    };
  }
}
