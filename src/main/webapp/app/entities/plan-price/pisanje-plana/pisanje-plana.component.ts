import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { IPlanPrice, PlanPrice } from '../plan-price.model';
import { finalize } from 'rxjs/operators';
import { PlanPriceService } from '../service/plan-price.service';
import { AccountService } from '../../../core/auth/account.service';

@Component({
  selector: 'jhi-pisanje-plana',
  templateUrl: './pisanje-plana.component.html',
  styleUrls: ['./pisanje-plana.component.scss'],
})
export class PisanjePlanaComponent implements OnInit {
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

  constructor(
    protected planPriceService: PlanPriceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planPrice }) => {
      // eslint-disable-next-line no-console
      console.log('aaa');
    });
  }
  save(): void {
    this.isSaving = true;
    const planPrice = this.createFromForm();
    if (planPrice.id !== undefined) {
      // eslint-disable-next-line no-console
      console.log('ovde', planPrice.id);
      this.subscribeToSaveResponse(this.planPriceService.update(planPrice));
    } else {
      this.accountService.getAuthenticationState().subscribe(account => {
        if (account) {
          // eslint-disable-next-line no-console
          console.log(account);
          if (account.authorities[0] === 'ROLE_VASPITAC') {
            const username = account.login; //ovo bi trebalo da radi, //todo prilikom menjanja statusa formularu potrebno je da se napravi dete
            this.subscribeToSaveResponse(this.planPriceService.createWithUsername(planPrice, username));
          }
        }
      });
    }
  }

  previousState(): void {
    window.history.back();
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
      //id: this.editForm.get(['id'])!.value,
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
