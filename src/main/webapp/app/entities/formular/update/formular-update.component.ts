import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { Formular, IFormular } from '../formular.model';
import { FormularService } from '../service/formular.service';
import { Adresa, IAdresa } from 'app/entities/adresa/adresa.model';
import { AdresaService } from 'app/entities/adresa/service/adresa.service';
import { IRoditelj } from 'app/entities/roditelj/roditelj.model';
import { RoditeljService } from 'app/entities/roditelj/service/roditelj.service';
import { StatusFormulara } from 'app/entities/enumerations/status-formulara.model';
import { TipGrupe } from 'app/entities/enumerations/tip-grupe.model';
import { RadniStatus } from '../../enumerations/radni-status.model';
import { IPodaciORoditeljima, PodaciORoditeljima } from '../../podaci-o-roditeljima/podaci-o-roditeljima.model';
import { PodaciORoditeljimaService } from '../../podaci-o-roditeljima/service/podaci-o-roditeljima.service';
import { AccountService } from '../../../core/auth/account.service';

@Component({
  selector: 'jhi-formular-update',
  templateUrl: './formular-update.component.html',
})
export class FormularUpdateComponent implements OnInit {
  isSaving = false;
  isSavedRoditelj1 = false;
  statusFormularaValues = Object.keys(StatusFormulara);
  tipGrupeValues = Object.keys(TipGrupe);
  radniStatusValues = Object.keys(RadniStatus);

  adresasCollection: IAdresa[] = [];
  roditeljsSharedCollection: IRoditelj[] = [];

  roditelj1: IPodaciORoditeljima = {};
  roditelj2: IPodaciORoditeljima = {};

  editForm = this.fb.group({
    id: [],
    mesecUpisa: [],
    jmbg: [],
    datumRodjenja: [],
    imeDeteta: [],
    mestoRodjenja: [],
    opstinaRodjenja: [],
    drzava: [],
    brDeceUPorodici: [],
    zdravstveniProblemi: [],
    zdravstveniUslovi: [],
    statusFormulara: [],
    tipGrupe: [],
    adresa: [],
    roditelj: [],
    ulica: [],
    mesto: [],
    broj: [],
  });

  editFormRoditelj1 = this.fb.group({
    id1: [],
    jmbg1: [],
    ime1: [],
    prezime1: [],
    telefon1: [],
    firma1: [],
    radnoVreme1: [],
    radniStatus1: [],
    formular1: [],
  });

  editFormRoditelj2 = this.fb.group({
    id2: [],
    jmbg2: [],
    ime2: [],
    prezime2: [],
    telefon2: [],
    firma2: [],
    radnoVreme2: [],
    radniStatus2: [],
    formular2: [],
  });

  constructor(
    protected formularService: FormularService,
    protected adresaService: AdresaService,
    protected roditeljService: RoditeljService,
    protected activatedRoute: ActivatedRoute,
    protected podaciORoditeljimaService: PodaciORoditeljimaService,
    protected fb: FormBuilder,
    protected accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formular }) => {
      this.updateForm(formular);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const podaciORoditeljima1 = this.createFromFormPodaci(1);
    const podaciORoditeljima2 = this.createFromFormPodaci(2);
    const formular = this.createFromForm();

    if (formular.id !== undefined) {
      this.subscribeToSaveResponse(this.formularService.update(formular));
    } else {
      this.subscribeToSaveResponseR(this.podaciORoditeljimaService.create(podaciORoditeljima1));
      this.subscribeToSaveResponseR(this.podaciORoditeljimaService.create(podaciORoditeljima2));
      this.accountService.getAuthenticationState().subscribe(account => {
        if (account) {
          if (account.authorities[0] === 'ROLE_RODITELJ') {
            this.subscribeToSaveResponse(this.formularService.create(formular, account.login));
          }
        }
      });
    }
  }

  trackAdresaById(_index: number, item: IAdresa): number {
    return item.id!;
  }

  trackRoditeljById(_index: number, item: IRoditelj): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormular>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected subscribeToSaveResponseR(result: Observable<HttpResponse<IPodaciORoditeljima>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: (res: HttpResponse<IPodaciORoditeljima>) => this.onSaveSuccessRoditelj(res),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }
  protected onSaveSuccessRoditelj(res: HttpResponse<IPodaciORoditeljima>): void {
    if (!this.isSavedRoditelj1) {
      this.roditelj1 = res.body!;
      this.isSavedRoditelj1 = true;
    } else {
      this.roditelj2 = res.body!;
    }
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(formular: IFormular): void {
    this.editForm.patchValue({
      id: formular.id,
      mesecUpisa: formular.mesecUpisa,
      jmbg: formular.jmbg,
      datumRodjenja: formular.datumRodjenja,
      imeDeteta: formular.imeDeteta,
      mestoRodjenja: formular.mestoRodjenja,
      opstinaRodjenja: formular.opstinaRodjenja,
      drzava: formular.drzava,
      brDeceUPorodici: formular.brDeceUPorodici,
      zdravstveniProblemi: formular.zdravstveniProblemi,
      zdravstveniUslovi: formular.zdravstveniUslovi,
      statusFormulara: formular.statusFormulara,
      tipGrupe: formular.tipGrupe,
      adresa: formular.adresa,
      roditelj: formular.roditelj,
      mesto: formular.adresa?.mesto,
      ulica: formular.adresa?.ulica,
      broj: formular.adresa?.broj,
    });

    this.adresasCollection = this.adresaService.addAdresaToCollectionIfMissing(this.adresasCollection, formular.adresa);
    this.roditeljsSharedCollection = this.roditeljService.addRoditeljToCollectionIfMissing(
      this.roditeljsSharedCollection,
      formular.roditelj
    );
  }

  protected loadRelationshipsOptions(): void {
    this.adresaService
      .query({ filter: 'formular-is-null' })
      .pipe(map((res: HttpResponse<IAdresa[]>) => res.body ?? []))
      .pipe(map((adresas: IAdresa[]) => this.adresaService.addAdresaToCollectionIfMissing(adresas, this.editForm.get('adresa')!.value)))
      .subscribe((adresas: IAdresa[]) => (this.adresasCollection = adresas));

    this.roditeljService
      .query()
      .pipe(map((res: HttpResponse<IRoditelj[]>) => res.body ?? []))
      .pipe(
        map((roditeljs: IRoditelj[]) =>
          this.roditeljService.addRoditeljToCollectionIfMissing(roditeljs, this.editForm.get('roditelj')!.value)
        )
      )
      .subscribe((roditeljs: IRoditelj[]) => (this.roditeljsSharedCollection = roditeljs));
  }

  protected createFromForm(): IFormular {
    const adresa = {
      ...new Adresa(),
      mesto: this.editForm.get(['mesto'])!.value,
      ulica: this.editForm.get(['ulica'])!.value,
      broj: this.editForm.get(['broj'])!.value,
    };
    const listPodaciORoditeljima = [this.roditelj2, this.roditelj1];
    const formular = {
      ...new Formular(),
      id: this.editForm.get(['id'])!.value,
      mesecUpisa: this.editForm.get(['mesecUpisa'])!.value,
      jmbg: this.editForm.get(['jmbg'])!.value,
      datumRodjenja: this.editForm.get(['datumRodjenja'])!.value,
      imeDeteta: this.editForm.get(['imeDeteta'])!.value,
      mestoRodjenja: this.editForm.get(['mestoRodjenja'])!.value,
      opstinaRodjenja: this.editForm.get(['opstinaRodjenja'])!.value,
      drzava: this.editForm.get(['drzava'])!.value,
      brDeceUPorodici: this.editForm.get(['brDeceUPorodici'])!.value,
      zdravstveniProblemi: this.editForm.get(['zdravstveniProblemi'])!.value,
      zdravstveniUslovi: this.editForm.get(['zdravstveniUslovi'])!.value,
      statusFormulara: StatusFormulara.NOV,
      tipGrupe: this.editForm.get(['tipGrupe'])!.value,
      adresa: {},
      roditelj: this.editForm.get(['roditelj'])!.value,
      podaciORoditeljimas: listPodaciORoditeljima,
    };
    formular.adresa = adresa;
    return formular;
  }

  protected createFromFormPodaci(num: number): IPodaciORoditeljima {
    if (num === 1) {
      return {
        ...new PodaciORoditeljima(),
        id: this.editFormRoditelj1.get(['id1'])!.value,
        jmbg: this.editFormRoditelj1.get(['jmbg1'])!.value,
        ime: this.editFormRoditelj1.get(['ime1'])!.value,
        prezime: this.editFormRoditelj1.get(['prezime1'])!.value,
        telefon: this.editFormRoditelj1.get(['telefon1'])!.value,
        firma: this.editFormRoditelj1.get(['firma1'])!.value,
        radnoVreme: this.editFormRoditelj1.get(['radnoVreme1'])!.value,
        radniStatus: this.editFormRoditelj1.get(['radniStatus1'])!.value,
        formular: this.editFormRoditelj1.get(['formular1'])!.value,
      };
    } else {
      return {
        ...new PodaciORoditeljima(),
        id: this.editFormRoditelj2.get(['id2'])!.value,
        jmbg: this.editFormRoditelj2.get(['jmbg2'])!.value,
        ime: this.editFormRoditelj2.get(['ime2'])!.value,
        prezime: this.editFormRoditelj2.get(['prezime2'])!.value,
        telefon: this.editFormRoditelj2.get(['telefon2'])!.value,
        firma: this.editFormRoditelj2.get(['firma2'])!.value,
        radnoVreme: this.editFormRoditelj2.get(['radnoVreme2'])!.value,
        radniStatus: this.editFormRoditelj2.get(['radniStatus2'])!.value,
        formular: this.editFormRoditelj2.get(['formular2'])!.value,
      };
    }
  }
}
