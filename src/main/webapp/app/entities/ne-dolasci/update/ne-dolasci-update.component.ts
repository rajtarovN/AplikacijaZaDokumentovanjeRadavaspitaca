import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INeDolasci, NeDolasci } from '../ne-dolasci.model';
import { NeDolasciService } from '../service/ne-dolasci.service';
import { IDete } from 'app/entities/dete/dete.model';
import { DeteService } from 'app/entities/dete/service/dete.service';
import { IDnevnik } from 'app/entities/dnevnik/dnevnik.model';
import { DnevnikService } from 'app/entities/dnevnik/service/dnevnik.service';

@Component({
  selector: 'jhi-ne-dolasci-update',
  templateUrl: './ne-dolasci-update.component.html',
})
export class NeDolasciUpdateComponent implements OnInit {
  isSaving = false;

  detesCollection: IDete[] = [];
  dnevniksSharedCollection: IDnevnik[] = [];

  editForm = this.fb.group({
    datum: [],
    razlog: [],
    id: [],
    dete: [],
    dnevnik: [],
  });

  constructor(
    protected neDolasciService: NeDolasciService,
    protected deteService: DeteService,
    protected dnevnikService: DnevnikService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ neDolasci }) => {
      this.updateForm(neDolasci);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const neDolasci = this.createFromForm();
    if (neDolasci.id !== undefined) {
      this.subscribeToSaveResponse(this.neDolasciService.update(neDolasci));
    } else {
      this.subscribeToSaveResponse(this.neDolasciService.create(neDolasci));
    }
  }

  trackDeteById(_index: number, item: IDete): number {
    return item.id!;
  }

  trackDnevnikById(_index: number, item: IDnevnik): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INeDolasci>>): void {
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

  protected updateForm(neDolasci: INeDolasci): void {
    this.editForm.patchValue({
      datum: neDolasci.datum,
      razlog: neDolasci.razlog,
      id: neDolasci.id,
      dete: neDolasci.dete,
      dnevnik: neDolasci.dnevnik,
    });

    this.detesCollection = this.deteService.addDeteToCollectionIfMissing(this.detesCollection, neDolasci.dete);
    this.dnevniksSharedCollection = this.dnevnikService.addDnevnikToCollectionIfMissing(this.dnevniksSharedCollection, neDolasci.dnevnik);
  }

  protected loadRelationshipsOptions(): void {
    this.deteService
      .query({ filter: 'nedolasci-is-null' })
      .pipe(map((res: HttpResponse<IDete[]>) => res.body ?? []))
      .pipe(map((detes: IDete[]) => this.deteService.addDeteToCollectionIfMissing(detes, this.editForm.get('dete')!.value)))
      .subscribe((detes: IDete[]) => (this.detesCollection = detes));

    this.dnevnikService
      .query()
      .pipe(map((res: HttpResponse<IDnevnik[]>) => res.body ?? []))
      .pipe(
        map((dnevniks: IDnevnik[]) => this.dnevnikService.addDnevnikToCollectionIfMissing(dnevniks, this.editForm.get('dnevnik')!.value))
      )
      .subscribe((dnevniks: IDnevnik[]) => (this.dnevniksSharedCollection = dnevniks));
  }

  protected createFromForm(): INeDolasci {
    return {
      ...new NeDolasci(),
      datum: this.editForm.get(['datum'])!.value,
      razlog: this.editForm.get(['razlog'])!.value,
      id: this.editForm.get(['id'])!.value,
      dete: this.editForm.get(['dete'])!.value,
      dnevnik: this.editForm.get(['dnevnik'])!.value,
    };
  }
}
