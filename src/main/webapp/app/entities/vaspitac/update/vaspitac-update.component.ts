import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVaspitac, Vaspitac } from '../vaspitac.model';
import { VaspitacService } from '../service/vaspitac.service';
import { IObjekat } from 'app/entities/objekat/objekat.model';
import { ObjekatService } from 'app/entities/objekat/service/objekat.service';

@Component({
  selector: 'jhi-vaspitac-update',
  templateUrl: './vaspitac-update.component.html',
})
export class VaspitacUpdateComponent implements OnInit {
  isSaving = false;

  objekatsSharedCollection: IObjekat[] = [];

  editForm = this.fb.group({
    datumZaposlenja: [],
    godineIskustva: [],
    opis: [],
    id: [],
    objekat: [],
  });

  constructor(
    protected vaspitacService: VaspitacService,
    protected objekatService: ObjekatService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vaspitac }) => {
      this.updateForm(vaspitac);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vaspitac = this.createFromForm();
    if (vaspitac.id !== undefined) {
      this.subscribeToSaveResponse(this.vaspitacService.update(vaspitac));
    } else {
      this.subscribeToSaveResponse(this.vaspitacService.create(vaspitac));
    }
  }

  trackObjekatById(_index: number, item: IObjekat): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVaspitac>>): void {
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

  protected updateForm(vaspitac: IVaspitac): void {
    this.editForm.patchValue({
      datumZaposlenja: vaspitac.datumZaposlenja,
      godineIskustva: vaspitac.godineIskustva,
      opis: vaspitac.opis,
      id: vaspitac.id,
      objekat: vaspitac.objekat,
    });

    this.objekatsSharedCollection = this.objekatService.addObjekatToCollectionIfMissing(this.objekatsSharedCollection, vaspitac.objekat);
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

  protected createFromForm(): IVaspitac {
    return {
      ...new Vaspitac(),
      datumZaposlenja: this.editForm.get(['datumZaposlenja'])!.value,
      godineIskustva: this.editForm.get(['godineIskustva'])!.value,
      opis: this.editForm.get(['opis'])!.value,
      id: this.editForm.get(['id'])!.value,
      objekat: this.editForm.get(['objekat'])!.value,
    };
  }
}
