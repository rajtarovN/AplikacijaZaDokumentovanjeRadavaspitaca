import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPotrebanMaterijal, PotrebanMaterijal } from '../potreban-materijal.model';
import { PotrebanMaterijalService } from '../service/potreban-materijal.service';
import { IObjekat } from 'app/entities/objekat/objekat.model';
import { ObjekatService } from 'app/entities/objekat/service/objekat.service';
import { StatusMaterijala } from 'app/entities/enumerations/status-materijala.model';

@Component({
  selector: 'jhi-potreban-materijal-update',
  templateUrl: './potreban-materijal-update.component.html',
})
export class PotrebanMaterijalUpdateComponent implements OnInit {
  isSaving = false;
  statusMaterijalaValues = Object.keys(StatusMaterijala);

  objekatsSharedCollection: IObjekat[] = [];

  editForm = this.fb.group({
    naziv: [],
    kolicina: [],
    id: [],
    statusMaterijala: [],
    objekat: [],
  });

  constructor(
    protected potrebanMaterijalService: PotrebanMaterijalService,
    protected objekatService: ObjekatService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ potrebanMaterijal }) => {
      this.updateForm(potrebanMaterijal);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const potrebanMaterijal = this.createFromForm();
    if (potrebanMaterijal.id !== undefined) {
      this.subscribeToSaveResponse(this.potrebanMaterijalService.update(potrebanMaterijal));
    } else {
      this.subscribeToSaveResponse(this.potrebanMaterijalService.create(potrebanMaterijal));
    }
  }

  trackObjekatById(_index: number, item: IObjekat): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPotrebanMaterijal>>): void {
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

  protected updateForm(potrebanMaterijal: IPotrebanMaterijal): void {
    this.editForm.patchValue({
      naziv: potrebanMaterijal.naziv,
      kolicina: potrebanMaterijal.kolicina,
      id: potrebanMaterijal.id,
      statusMaterijala: potrebanMaterijal.statusMaterijala,
      objekat: potrebanMaterijal.objekat,
    });

    this.objekatsSharedCollection = this.objekatService.addObjekatToCollectionIfMissing(
      this.objekatsSharedCollection,
      potrebanMaterijal.objekat
    );
  }

  protected loadRelationshipsOptions(): void {
    this.objekatService
      .query()
      .pipe(map((res: HttpResponse<IObjekat[]>) => res.body ?? []))
      .pipe(
        map((objekats: IObjekat[]) => this.objekatService.addObjekatToCollectionIfMissing(objekats, this.editForm.get('objekat')!.value))
      )
      .subscribe((objekats: IObjekat[]) => (this.objekatsSharedCollection = objekats));
  }

  protected createFromForm(): IPotrebanMaterijal {
    return {
      ...new PotrebanMaterijal(),
      naziv: this.editForm.get(['naziv'])!.value,
      kolicina: this.editForm.get(['kolicina'])!.value,
      id: this.editForm.get(['id'])!.value,
      statusMaterijala: this.editForm.get(['statusMaterijala'])!.value,
      objekat: this.editForm.get(['objekat'])!.value,
    };
  }
}
