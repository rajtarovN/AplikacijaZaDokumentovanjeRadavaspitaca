import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IGrupa, Grupa } from '../grupa.model';
import { GrupaService } from '../service/grupa.service';
import { TipGrupe } from 'app/entities/enumerations/tip-grupe.model';

@Component({
  selector: 'jhi-grupa-update',
  templateUrl: './grupa-update.component.html',
})
export class GrupaUpdateComponent implements OnInit {
  isSaving = false;
  tipGrupeValues = Object.keys(TipGrupe);

  editForm = this.fb.group({
    id: [],
    tipGrupe: [],
  });

  constructor(protected grupaService: GrupaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ grupa }) => {
      this.updateForm(grupa);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const grupa = this.createFromForm();
    if (grupa.id !== undefined) {
      this.subscribeToSaveResponse(this.grupaService.update(grupa));
    } else {
      this.subscribeToSaveResponse(this.grupaService.create(grupa));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGrupa>>): void {
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

  protected updateForm(grupa: IGrupa): void {
    this.editForm.patchValue({
      id: grupa.id,
      tipGrupe: grupa.tipGrupe,
    });
  }

  protected createFromForm(): IGrupa {
    return {
      ...new Grupa(),
      id: this.editForm.get(['id'])!.value,
      tipGrupe: this.editForm.get(['tipGrupe'])!.value,
    };
  }
}
