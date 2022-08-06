import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPlanPrice, PlanPrice } from '../plan-price.model';
import { PlanPriceService } from '../service/plan-price.service';

@Component({
  selector: 'jhi-plan-price-update',
  templateUrl: './plan-price-update.component.html',
})
export class PlanPriceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    izvoriSaznanja: [],
    nazivTeme: [],
    pocetnaIdeja: [],
    datumPocetka: [],
    nacinUcescaVaspitaca: [],
    materijali: [],
    ucescePorodice: [],
    mesta: [],
    datumZavrsetka: [],
  });

  constructor(protected planPriceService: PlanPriceService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planPrice }) => {
      this.updateForm(planPrice);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const planPrice = this.createFromForm();
    if (planPrice.id !== undefined) {
      this.subscribeToSaveResponse(this.planPriceService.update(planPrice));
    } else {
      this.subscribeToSaveResponse(this.planPriceService.create(planPrice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlanPrice>>): void {
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

  protected updateForm(planPrice: IPlanPrice): void {
    this.editForm.patchValue({
      id: planPrice.id,
      izvoriSaznanja: planPrice.izvoriSaznanja,
      nazivTeme: planPrice.nazivTeme,
      pocetnaIdeja: planPrice.pocetnaIdeja,
      datumPocetka: planPrice.datumPocetka,
      nacinUcescaVaspitaca: planPrice.nacinUcescaVaspitaca,
      materijali: planPrice.materijali,
      ucescePorodice: planPrice.ucescePorodice,
      mesta: planPrice.mesta,
      datumZavrsetka: planPrice.datumZavrsetka,
    });
  }

  protected createFromForm(): IPlanPrice {
    // eslint-disable-next-line no-console
    console.log('aaa', this.editForm.get(['id'])!.value, 'aaa');
    return {
      ...new PlanPrice(),
      id: this.editForm.get(['id'])!.value,
      izvoriSaznanja: this.editForm.get(['izvoriSaznanja'])!.value,
      nazivTeme: this.editForm.get(['nazivTeme'])!.value,
      pocetnaIdeja: this.editForm.get(['pocetnaIdeja'])!.value,
      datumPocetka: this.editForm.get(['datumPocetka'])!.value,
      nacinUcescaVaspitaca: this.editForm.get(['nacinUcescaVaspitaca'])!.value,
      materijali: this.editForm.get(['materijali'])!.value,
      ucescePorodice: this.editForm.get(['ucescePorodice'])!.value,
      mesta: this.editForm.get(['mesta'])!.value,
      datumZavrsetka: this.editForm.get(['datumZavrsetka'])!.value,
    };
  }
}
