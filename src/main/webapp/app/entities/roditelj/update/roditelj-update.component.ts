import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRoditelj, Roditelj } from '../roditelj.model';
import { RoditeljService } from '../service/roditelj.service';

@Component({
  selector: 'jhi-roditelj-update',
  templateUrl: './roditelj-update.component.html',
})
export class RoditeljUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected roditeljService: RoditeljService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roditelj }) => {
      this.updateForm(roditelj);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const roditelj = this.createFromForm();
    if (roditelj.id !== undefined) {
      this.subscribeToSaveResponse(this.roditeljService.update(roditelj));
    } else {
      this.subscribeToSaveResponse(this.roditeljService.create(roditelj));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoditelj>>): void {
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

  protected updateForm(roditelj: IRoditelj): void {
    this.editForm.patchValue({
      id: roditelj.id,
    });
  }

  protected createFromForm(): IRoditelj {
    return {
      ...new Roditelj(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
