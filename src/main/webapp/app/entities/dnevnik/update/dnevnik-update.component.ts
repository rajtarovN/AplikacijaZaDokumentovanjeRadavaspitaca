import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDnevnik, Dnevnik } from '../dnevnik.model';
import { DnevnikService } from '../service/dnevnik.service';
import { IGrupa } from 'app/entities/grupa/grupa.model';
import { GrupaService } from 'app/entities/grupa/service/grupa.service';
import { IVaspitac } from 'app/entities/vaspitac/vaspitac.model';
import { VaspitacService } from 'app/entities/vaspitac/service/vaspitac.service';

@Component({
  selector: 'jhi-dnevnik-update',
  templateUrl: './dnevnik-update.component.html',
})
export class DnevnikUpdateComponent implements OnInit {
  isSaving = false;

  grupasCollection: IGrupa[] = [];
  vaspitacsSharedCollection: IVaspitac[] = [];

  editForm = this.fb.group({
    pocetakVazenja: [],
    krajVazenja: [],
    id: [],
    grupa: [],
    vaspitacs: [],
  });

  constructor(
    protected dnevnikService: DnevnikService,
    protected grupaService: GrupaService,
    protected vaspitacService: VaspitacService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dnevnik }) => {
      this.updateForm(dnevnik);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dnevnik = this.createFromForm();
    if (dnevnik.id !== undefined) {
      this.subscribeToSaveResponse(this.dnevnikService.update(dnevnik));
    } else {
      this.subscribeToSaveResponse(this.dnevnikService.create(dnevnik));
    }
  }

  trackGrupaById(_index: number, item: IGrupa): number {
    return item.id!;
  }

  trackVaspitacById(_index: number, item: IVaspitac): number {
    return item.id!;
  }

  getSelectedVaspitac(option: IVaspitac, selectedVals?: IVaspitac[]): IVaspitac {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDnevnik>>): void {
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

  protected updateForm(dnevnik: IDnevnik): void {
    this.editForm.patchValue({
      pocetakVazenja: dnevnik.pocetakVazenja,
      krajVazenja: dnevnik.krajVazenja,
      id: dnevnik.id,
      grupa: dnevnik.grupa,
      vaspitacs: dnevnik.vaspitacs,
    });

    this.grupasCollection = this.grupaService.addGrupaToCollectionIfMissing(this.grupasCollection, dnevnik.grupa);
    this.vaspitacsSharedCollection = this.vaspitacService.addVaspitacToCollectionIfMissing(
      this.vaspitacsSharedCollection,
      ...(dnevnik.vaspitacs ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.grupaService
      .query({ filter: 'dnevnik-is-null' })
      .pipe(map((res: HttpResponse<IGrupa[]>) => res.body ?? []))
      .pipe(map((grupas: IGrupa[]) => this.grupaService.addGrupaToCollectionIfMissing(grupas, this.editForm.get('grupa')!.value)))
      .subscribe((grupas: IGrupa[]) => (this.grupasCollection = grupas));

    this.vaspitacService
      .query()
      .pipe(map((res: HttpResponse<IVaspitac[]>) => res.body ?? []))
      .pipe(
        map((vaspitacs: IVaspitac[]) =>
          this.vaspitacService.addVaspitacToCollectionIfMissing(vaspitacs, ...(this.editForm.get('vaspitacs')!.value ?? []))
        )
      )
      .subscribe((vaspitacs: IVaspitac[]) => (this.vaspitacsSharedCollection = vaspitacs));
  }

  protected createFromForm(): IDnevnik {
    return {
      ...new Dnevnik(),
      pocetakVazenja: this.editForm.get(['pocetakVazenja'])!.value,
      krajVazenja: this.editForm.get(['krajVazenja'])!.value,
      id: this.editForm.get(['id'])!.value,
      grupa: this.editForm.get(['grupa'])!.value,
      vaspitacs: this.editForm.get(['vaspitacs'])!.value,
    };
  }
}
