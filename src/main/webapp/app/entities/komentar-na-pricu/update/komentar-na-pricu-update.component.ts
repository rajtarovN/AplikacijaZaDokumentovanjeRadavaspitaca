import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IKomentarNaPricu, KomentarNaPricu } from '../komentar-na-pricu.model';
import { KomentarNaPricuService } from '../service/komentar-na-pricu.service';
import { IPrica } from 'app/entities/prica/prica.model';
import { PricaService } from 'app/entities/prica/service/prica.service';

@Component({
  selector: 'jhi-komentar-na-pricu-update',
  templateUrl: './komentar-na-pricu-update.component.html',
})
export class KomentarNaPricuUpdateComponent implements OnInit {
  isSaving = false;

  pricasSharedCollection: IPrica[] = [];

  editForm = this.fb.group({
    id: [],
    tekst: [],
    datum: [],
    prica: [],
  });

  constructor(
    protected komentarNaPricuService: KomentarNaPricuService,
    protected pricaService: PricaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ komentarNaPricu }) => {
      this.updateForm(komentarNaPricu);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const komentarNaPricu = this.createFromForm();
    if (komentarNaPricu.id !== undefined) {
      this.subscribeToSaveResponse(this.komentarNaPricuService.update(komentarNaPricu));
    } else {
      this.subscribeToSaveResponse(this.komentarNaPricuService.create(komentarNaPricu));
    }
  }

  trackPricaById(_index: number, item: IPrica): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKomentarNaPricu>>): void {
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

  protected updateForm(komentarNaPricu: IKomentarNaPricu): void {
    this.editForm.patchValue({
      id: komentarNaPricu.id,
      tekst: komentarNaPricu.tekst,
      datum: komentarNaPricu.datum,
      prica: komentarNaPricu.prica,
    });

    this.pricasSharedCollection = this.pricaService.addPricaToCollectionIfMissing(this.pricasSharedCollection, komentarNaPricu.prica);
  }

  protected loadRelationshipsOptions(): void {
    this.pricaService
      .query()
      .pipe(map((res: HttpResponse<IPrica[]>) => res.body ?? []))
      .pipe(map((pricas: IPrica[]) => this.pricaService.addPricaToCollectionIfMissing(pricas, this.editForm.get('prica')!.value)))
      .subscribe((pricas: IPrica[]) => (this.pricasSharedCollection = pricas));
  }

  protected createFromForm(): IKomentarNaPricu {
    return {
      ...new KomentarNaPricu(),
      id: this.editForm.get(['id'])!.value,
      tekst: this.editForm.get(['tekst'])!.value,
      datum: this.editForm.get(['datum'])!.value,
      prica: this.editForm.get(['prica'])!.value,
    };
  }
}
