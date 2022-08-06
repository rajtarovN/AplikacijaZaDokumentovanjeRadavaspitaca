import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKonacnaPrica } from '../konacna-prica.model';
import { FormBuilder } from '@angular/forms';
import { IKomentarNaPricu, KomentarNaPricu } from '../../komentar-na-pricu/komentar-na-pricu.model';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { Dayjs } from 'dayjs';
import { Prica } from '../../prica/prica.model';
import { KomentarNaPricuService } from '../../komentar-na-pricu/service/komentar-na-pricu.service';

@Component({
  selector: 'jhi-konacna-prica-detail',
  templateUrl: './konacna-prica-detail.component.html',
})
export class KonacnaPricaDetailComponent implements OnInit {
  konacnaPrica: IKonacnaPrica | null = null;
  idPrice?: number;
  isSaving = false;
  editForm = this.fb.group({
    tekstKomentara: [],
  });

  constructor(
    protected komentarNaPricuService: KomentarNaPricuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.idPrice = Number(localStorage.getItem('idPrice'));
    this.activatedRoute.data.subscribe(({ konacnaPrica }) => {
      this.konacnaPrica = konacnaPrica;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const komentarNaPricu = this.createFromForm();
    this.subscribeToSaveResponse(this.komentarNaPricuService.create(komentarNaPricu));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKomentarNaPricu>>): void {
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

  protected createFromForm(): IKomentarNaPricu {
    return {
      ...new KomentarNaPricu(),
      tekst: this.editForm.get(['tekstKomentara'])!.value,
      datum: null,
      prica: new Prica(this.idPrice),
    };
  }
}
