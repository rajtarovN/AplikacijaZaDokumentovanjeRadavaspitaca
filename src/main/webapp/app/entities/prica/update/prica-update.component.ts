import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPrica, Prica } from '../prica.model';
import { PricaService } from '../service/prica.service';
import { IPlanPrice } from 'app/entities/plan-price/plan-price.model';
import { PlanPriceService } from 'app/entities/plan-price/service/plan-price.service';
import { IKonacnaPrica } from 'app/entities/konacna-prica/konacna-prica.model';
import { KonacnaPricaService } from 'app/entities/konacna-prica/service/konacna-prica.service';
import { IDnevnik } from 'app/entities/dnevnik/dnevnik.model';
import { DnevnikService } from 'app/entities/dnevnik/service/dnevnik.service';

@Component({
  selector: 'jhi-prica-update',
  templateUrl: './prica-update.component.html',
})
export class PricaUpdateComponent implements OnInit {
  isSaving = false;

  planPricesCollection: IPlanPrice[] = [];
  konacnaPricasCollection: IKonacnaPrica[] = [];
  dnevniksSharedCollection: IDnevnik[] = [];

  editForm = this.fb.group({
    id: [],
    planPrice: [],
    konacnaPrica: [],
    dnevnik: [],
  });

  constructor(
    protected pricaService: PricaService,
    protected planPriceService: PlanPriceService,
    protected konacnaPricaService: KonacnaPricaService,
    protected dnevnikService: DnevnikService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prica }) => {
      this.updateForm(prica);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prica = this.createFromForm();
    if (prica.id !== undefined) {
      this.subscribeToSaveResponse(this.pricaService.update(prica));
    } else {
      this.subscribeToSaveResponse(this.pricaService.create(prica));
    }
  }

  trackPlanPriceById(_index: number, item: IPlanPrice): number {
    return item.id!;
  }

  trackKonacnaPricaById(_index: number, item: IKonacnaPrica): number {
    return item.id!;
  }

  trackDnevnikById(_index: number, item: IDnevnik): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrica>>): void {
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

  protected updateForm(prica: IPrica): void {
    this.editForm.patchValue({
      id: prica.id,
      planPrice: prica.planPrice,
      konacnaPrica: prica.konacnaPrica,
      dnevnik: prica.dnevnik,
    });

    this.planPricesCollection = this.planPriceService.addPlanPriceToCollectionIfMissing(this.planPricesCollection, prica.planPrice);
    this.konacnaPricasCollection = this.konacnaPricaService.addKonacnaPricaToCollectionIfMissing(
      this.konacnaPricasCollection,
      prica.konacnaPrica
    );
    this.dnevniksSharedCollection = this.dnevnikService.addDnevnikToCollectionIfMissing(this.dnevniksSharedCollection, prica.dnevnik);
  }

  protected loadRelationshipsOptions(): void {
    this.planPriceService
      .query({ filter: 'prica-is-null' })
      .pipe(map((res: HttpResponse<IPlanPrice[]>) => res.body ?? []))
      .pipe(
        map((planPrices: IPlanPrice[]) =>
          this.planPriceService.addPlanPriceToCollectionIfMissing(planPrices, this.editForm.get('planPrice')!.value)
        )
      )
      .subscribe((planPrices: IPlanPrice[]) => (this.planPricesCollection = planPrices));

    this.konacnaPricaService
      .query({ filter: 'prica-is-null' })
      .pipe(map((res: HttpResponse<IKonacnaPrica[]>) => res.body ?? []))
      .pipe(
        map((konacnaPricas: IKonacnaPrica[]) =>
          this.konacnaPricaService.addKonacnaPricaToCollectionIfMissing(konacnaPricas, this.editForm.get('konacnaPrica')!.value)
        )
      )
      .subscribe((konacnaPricas: IKonacnaPrica[]) => (this.konacnaPricasCollection = konacnaPricas));

    this.dnevnikService
      .query()
      .pipe(map((res: HttpResponse<IDnevnik[]>) => res.body ?? []))
      .pipe(
        map((dnevniks: IDnevnik[]) => this.dnevnikService.addDnevnikToCollectionIfMissing(dnevniks, this.editForm.get('dnevnik')!.value))
      )
      .subscribe((dnevniks: IDnevnik[]) => (this.dnevniksSharedCollection = dnevniks));
  }

  protected createFromForm(): IPrica {
    return {
      ...new Prica(),
      id: this.editForm.get(['id'])!.value,
      planPrice: this.editForm.get(['planPrice'])!.value,
      konacnaPrica: this.editForm.get(['konacnaPrica'])!.value,
      dnevnik: this.editForm.get(['dnevnik'])!.value,
    };
  }
}
