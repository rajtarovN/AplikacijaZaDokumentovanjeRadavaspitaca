import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFormular, Formular } from '../formular.model';
import { FormularService } from '../service/formular.service';
import { IAdresa } from 'app/entities/adresa/adresa.model';
import { AdresaService } from 'app/entities/adresa/service/adresa.service';
import { IRoditelj } from 'app/entities/roditelj/roditelj.model';
import { RoditeljService } from 'app/entities/roditelj/service/roditelj.service';
import { StatusFormulara } from 'app/entities/enumerations/status-formulara.model';
import { TipGrupe } from 'app/entities/enumerations/tip-grupe.model';

@Component({
  selector: 'jhi-formular-update',
  templateUrl: './formular-update.component.html',
})
export class FormularUpdateComponent implements OnInit {
  isSaving = false;
  statusFormularaValues = Object.keys(StatusFormulara);
  tipGrupeValues = Object.keys(TipGrupe);

  adresasCollection: IAdresa[] = [];
  roditeljsSharedCollection: IRoditelj[] = [];

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
  });

  constructor(
    protected formularService: FormularService,
    protected adresaService: AdresaService,
    protected roditeljService: RoditeljService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
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
    const formular = this.createFromForm();
    if (formular.id !== undefined) {
      this.subscribeToSaveResponse(this.formularService.update(formular));
    } else {
      this.subscribeToSaveResponse(this.formularService.create(formular));
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

  protected onSaveSuccess(): void {
    this.previousState();
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
    return {
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
      statusFormulara: this.editForm.get(['statusFormulara'])!.value,
      tipGrupe: this.editForm.get(['tipGrupe'])!.value,
      adresa: this.editForm.get(['adresa'])!.value,
      roditelj: this.editForm.get(['roditelj'])!.value,
    };
  }
}
