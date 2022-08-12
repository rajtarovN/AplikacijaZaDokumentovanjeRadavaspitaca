import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IZapazanjeUVeziDeteta, ZapazanjeUVeziDeteta } from '../zapazanje-u-vezi-deteta.model';
import { ZapazanjeUVeziDetetaService } from '../service/zapazanje-u-vezi-deteta.service';
import { IVaspitac } from 'app/entities/vaspitac/vaspitac.model';
import { VaspitacService } from 'app/entities/vaspitac/service/vaspitac.service';
import { IDete } from 'app/entities/dete/dete.model';
import { DeteService } from 'app/entities/dete/service/dete.service';
import { Account } from '../../../core/auth/account.model';
import { AccountService } from '../../../core/auth/account.service';

@Component({
  selector: 'jhi-zapazanje-u-vezi-deteta-update',
  templateUrl: './zapazanje-u-vezi-deteta-update.component.html',
})
export class ZapazanjeUVeziDetetaUpdateComponent implements OnInit {
  isSaving = false;

  vaspitacsCollection: IVaspitac[] = [];
  detesSharedCollection: IDete[] = [];

  editForm = this.fb.group({
    id: [],
    interesovanja: [],
    prednosti: [],
    mane: [],
    predlozi: [],
    datum: [],
    vaspitac: [],
    dete: [],
  });

  constructor(
    protected zapazanjeUVeziDetetaService: ZapazanjeUVeziDetetaService,
    protected vaspitacService: VaspitacService,
    protected deteService: DeteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zapazanjeUVeziDeteta }) => {
      this.updateForm(zapazanjeUVeziDeteta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const zapazanjeUVeziDeteta = this.createFromForm();
    zapazanjeUVeziDeteta.dete = { id: Number(localStorage.getItem('dete')) };

    this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        // eslint-disable-next-line no-console
        console.log(account);
        if (account.authorities[0] === 'ROLE_VASPITAC' || account.authorities[0] === 'ROLE_PEDAGOG') {
          //zapazanjeUVeziDeteta.vaspitac = { user: { login: account.login } };
          zapazanjeUVeziDeteta.user = { login: account.login };
        }
      }
    });

    if (zapazanjeUVeziDeteta.id !== undefined) {
      this.subscribeToSaveResponse(this.zapazanjeUVeziDetetaService.update(zapazanjeUVeziDeteta));
    } else {
      this.subscribeToSaveResponse(this.zapazanjeUVeziDetetaService.create(zapazanjeUVeziDeteta));
    }
  }

  trackVaspitacById(_index: number, item: IVaspitac): number {
    return item.id!;
  }

  trackDeteById(_index: number, item: IDete): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IZapazanjeUVeziDeteta>>): void {
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

  protected updateForm(zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta): void {
    this.editForm.patchValue({
      id: zapazanjeUVeziDeteta.id,
      interesovanja: zapazanjeUVeziDeteta.interesovanja,
      prednosti: zapazanjeUVeziDeteta.prednosti,
      mane: zapazanjeUVeziDeteta.mane,
      predlozi: zapazanjeUVeziDeteta.predlozi,
      datum: zapazanjeUVeziDeteta.datum,
      vaspitac: zapazanjeUVeziDeteta.vaspitac,
      dete: zapazanjeUVeziDeteta.dete,
      user: zapazanjeUVeziDeteta.user,
    });

    this.vaspitacsCollection = this.vaspitacService.addVaspitacToCollectionIfMissing(
      this.vaspitacsCollection,
      zapazanjeUVeziDeteta.vaspitac
    );
    this.detesSharedCollection = this.deteService.addDeteToCollectionIfMissing(this.detesSharedCollection, zapazanjeUVeziDeteta.dete);
  }

  protected loadRelationshipsOptions(): void {
    this.vaspitacService
      .query({ filter: 'zapazanjeuvezideteta-is-null' })
      .pipe(map((res: HttpResponse<IVaspitac[]>) => res.body ?? []))
      .pipe(
        map((vaspitacs: IVaspitac[]) =>
          this.vaspitacService.addVaspitacToCollectionIfMissing(vaspitacs, this.editForm.get('vaspitac')!.value)
        )
      )
      .subscribe((vaspitacs: IVaspitac[]) => (this.vaspitacsCollection = vaspitacs));

    this.deteService
      .query()
      .pipe(map((res: HttpResponse<IDete[]>) => res.body ?? []))
      .pipe(map((detes: IDete[]) => this.deteService.addDeteToCollectionIfMissing(detes, this.editForm.get('dete')!.value)))
      .subscribe((detes: IDete[]) => (this.detesSharedCollection = detes));
  }

  protected createFromForm(): IZapazanjeUVeziDeteta {
    return {
      ...new ZapazanjeUVeziDeteta(),
      id: this.editForm.get(['id'])!.value,
      interesovanja: this.editForm.get(['interesovanja'])!.value,
      prednosti: this.editForm.get(['prednosti'])!.value,
      mane: this.editForm.get(['mane'])!.value,
      predlozi: this.editForm.get(['predlozi'])!.value,
      datum: this.editForm.get(['datum'])!.value,
      vaspitac: this.editForm.get(['vaspitac'])!.value,
      dete: this.editForm.get(['dete'])!.value,
      user: this.editForm.get(['vaspitac'])!.value,
    };
  }
}
