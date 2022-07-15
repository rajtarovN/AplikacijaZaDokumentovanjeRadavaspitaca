import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDete, Dete } from '../dete.model';
import { DeteService } from '../service/dete.service';
import { IFormular } from 'app/entities/formular/formular.model';
import { FormularService } from 'app/entities/formular/service/formular.service';
import { IRoditelj } from 'app/entities/roditelj/roditelj.model';
import { RoditeljService } from 'app/entities/roditelj/service/roditelj.service';
import { IGrupa } from 'app/entities/grupa/grupa.model';
import { GrupaService } from 'app/entities/grupa/service/grupa.service';

@Component({
  selector: 'jhi-dete-update',
  templateUrl: './dete-update.component.html',
})
export class DeteUpdateComponent implements OnInit {
  isSaving = false;

  formularsCollection: IFormular[] = [];
  roditeljsSharedCollection: IRoditelj[] = [];
  grupasSharedCollection: IGrupa[] = [];

  editForm = this.fb.group({
    visina: [],
    tezina: [],
    id: [],
    formular: [],
    roditelj: [],
    grupa: [],
  });

  constructor(
    protected deteService: DeteService,
    protected formularService: FormularService,
    protected roditeljService: RoditeljService,
    protected grupaService: GrupaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dete }) => {
      this.updateForm(dete);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dete = this.createFromForm();
    if (dete.id !== undefined) {
      this.subscribeToSaveResponse(this.deteService.update(dete));
    } else {
      this.subscribeToSaveResponse(this.deteService.create(dete));
    }
  }

  trackFormularById(_index: number, item: IFormular): number {
    return item.id!;
  }

  trackRoditeljById(_index: number, item: IRoditelj): number {
    return item.id!;
  }

  trackGrupaById(_index: number, item: IGrupa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDete>>): void {
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

  protected updateForm(dete: IDete): void {
    this.editForm.patchValue({
      visina: dete.visina,
      tezina: dete.tezina,
      id: dete.id,
      formular: dete.formular,
      roditelj: dete.roditelj,
      grupa: dete.grupa,
    });

    this.formularsCollection = this.formularService.addFormularToCollectionIfMissing(this.formularsCollection, dete.formular);
    this.roditeljsSharedCollection = this.roditeljService.addRoditeljToCollectionIfMissing(this.roditeljsSharedCollection, dete.roditelj);
    this.grupasSharedCollection = this.grupaService.addGrupaToCollectionIfMissing(this.grupasSharedCollection, dete.grupa);
  }

  protected loadRelationshipsOptions(): void {
    this.formularService
      .query({ filter: 'dete-is-null' })
      .pipe(map((res: HttpResponse<IFormular[]>) => res.body ?? []))
      .pipe(
        map((formulars: IFormular[]) =>
          this.formularService.addFormularToCollectionIfMissing(formulars, this.editForm.get('formular')!.value)
        )
      )
      .subscribe((formulars: IFormular[]) => (this.formularsCollection = formulars));

    this.roditeljService
      .query()
      .pipe(map((res: HttpResponse<IRoditelj[]>) => res.body ?? []))
      .pipe(
        map((roditeljs: IRoditelj[]) =>
          this.roditeljService.addRoditeljToCollectionIfMissing(roditeljs, this.editForm.get('roditelj')!.value)
        )
      )
      .subscribe((roditeljs: IRoditelj[]) => (this.roditeljsSharedCollection = roditeljs));

    this.grupaService
      .query()
      .pipe(map((res: HttpResponse<IGrupa[]>) => res.body ?? []))
      .pipe(map((grupas: IGrupa[]) => this.grupaService.addGrupaToCollectionIfMissing(grupas, this.editForm.get('grupa')!.value)))
      .subscribe((grupas: IGrupa[]) => (this.grupasSharedCollection = grupas));
  }

  protected createFromForm(): IDete {
    return {
      ...new Dete(),
      visina: this.editForm.get(['visina'])!.value,
      tezina: this.editForm.get(['tezina'])!.value,
      id: this.editForm.get(['id'])!.value,
      formular: this.editForm.get(['formular'])!.value,
      roditelj: this.editForm.get(['roditelj'])!.value,
      grupa: this.editForm.get(['grupa'])!.value,
    };
  }
}
