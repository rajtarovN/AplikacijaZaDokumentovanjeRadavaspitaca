import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPodaciORoditeljima, PodaciORoditeljima } from '../podaci-o-roditeljima.model';
import { PodaciORoditeljimaService } from '../service/podaci-o-roditeljima.service';
import { IFormular } from 'app/entities/formular/formular.model';
import { FormularService } from 'app/entities/formular/service/formular.service';
import { RadniStatus } from 'app/entities/enumerations/radni-status.model';

@Component({
  selector: 'jhi-podaci-o-roditeljima-update',
  templateUrl: './podaci-o-roditeljima-update.component.html',
})
export class PodaciORoditeljimaUpdateComponent implements OnInit {
  isSaving = false;
  radniStatusValues = Object.keys(RadniStatus);

  formularsSharedCollection: IFormular[] = [];

  editForm = this.fb.group({
    id: [],
    jmbg: [
      null,
      [
        Validators.required,
        Validators.minLength(13),
        Validators.maxLength(13),
        Validators.pattern('[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
      ],
    ],
    ime: [],
    prezime: [],
    telefon: [],
    firma: [],
    radnoVreme: [],
    radniStatus: [],
    formular: [],
  });

  constructor(
    protected podaciORoditeljimaService: PodaciORoditeljimaService,
    protected formularService: FormularService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ podaciORoditeljima }) => {
      this.updateForm(podaciORoditeljima);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const podaciORoditeljima = this.createFromForm();
    if (podaciORoditeljima.id !== undefined) {
      this.subscribeToSaveResponse(this.podaciORoditeljimaService.update(podaciORoditeljima));
    } else {
      this.subscribeToSaveResponse(this.podaciORoditeljimaService.create(podaciORoditeljima));
    }
  }

  trackFormularById(_index: number, item: IFormular): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPodaciORoditeljima>>): void {
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

  protected updateForm(podaciORoditeljima: IPodaciORoditeljima): void {
    this.editForm.patchValue({
      id: podaciORoditeljima.id,
      jmbg: podaciORoditeljima.jmbg,
      ime: podaciORoditeljima.ime,
      prezime: podaciORoditeljima.prezime,
      telefon: podaciORoditeljima.telefon,
      firma: podaciORoditeljima.firma,
      radnoVreme: podaciORoditeljima.radnoVreme,
      radniStatus: podaciORoditeljima.radniStatus,
      formular: podaciORoditeljima.formular,
    });

    this.formularsSharedCollection = this.formularService.addFormularToCollectionIfMissing(
      this.formularsSharedCollection,
      podaciORoditeljima.formular
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formularService
      .query()
      .pipe(map((res: HttpResponse<IFormular[]>) => res.body ?? []))
      .pipe(
        map((formulars: IFormular[]) =>
          this.formularService.addFormularToCollectionIfMissing(formulars, this.editForm.get('formular')!.value)
        )
      )
      .subscribe((formulars: IFormular[]) => (this.formularsSharedCollection = formulars));
  }

  protected createFromForm(): IPodaciORoditeljima {
    return {
      ...new PodaciORoditeljima(),
      id: this.editForm.get(['id'])!.value,
      jmbg: this.editForm.get(['jmbg'])!.value,
      ime: this.editForm.get(['ime'])!.value,
      prezime: this.editForm.get(['prezime'])!.value,
      telefon: this.editForm.get(['telefon'])!.value,
      firma: this.editForm.get(['firma'])!.value,
      radnoVreme: this.editForm.get(['radnoVreme'])!.value,
      radniStatus: this.editForm.get(['radniStatus'])!.value,
      formular: this.editForm.get(['formular'])!.value,
    };
  }
}
