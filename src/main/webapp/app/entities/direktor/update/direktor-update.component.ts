import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDirektor, Direktor } from '../direktor.model';
import { DirektorService } from '../service/direktor.service';

@Component({
  selector: 'jhi-direktor-update',
  templateUrl: './direktor-update.component.html',
})
export class DirektorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected direktorService: DirektorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ direktor }) => {
      this.updateForm(direktor);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const direktor = this.createFromForm();
    if (direktor.id !== undefined) {
      this.subscribeToSaveResponse(this.direktorService.update(direktor));
    } else {
      this.subscribeToSaveResponse(this.direktorService.create(direktor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDirektor>>): void {
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

  protected updateForm(direktor: IDirektor): void {
    this.editForm.patchValue({
      id: direktor.id,
    });
  }

  protected createFromForm(): IDirektor {
    return {
      ...new Direktor(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
