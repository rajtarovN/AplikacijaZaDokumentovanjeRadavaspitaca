import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPedagog, Pedagog } from '../pedagog.model';
import { PedagogService } from '../service/pedagog.service';

@Component({
  selector: 'jhi-pedagog-update',
  templateUrl: './pedagog-update.component.html',
})
export class PedagogUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected pedagogService: PedagogService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pedagog }) => {
      this.updateForm(pedagog);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pedagog = this.createFromForm();
    if (pedagog.id !== undefined) {
      this.subscribeToSaveResponse(this.pedagogService.update(pedagog));
    } else {
      this.subscribeToSaveResponse(this.pedagogService.create(pedagog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPedagog>>): void {
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

  protected updateForm(pedagog: IPedagog): void {
    this.editForm.patchValue({
      id: pedagog.id,
    });
  }

  protected createFromForm(): IPedagog {
    return {
      ...new Pedagog(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
