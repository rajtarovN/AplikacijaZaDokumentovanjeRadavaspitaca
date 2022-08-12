import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKonacnaPrica } from '../konacna-prica.model';
import { FormBuilder } from '@angular/forms';
import { IKomentarNaPricu, KomentarNaPricu } from '../../komentar-na-pricu/komentar-na-pricu.model';
import { Observable } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { Dayjs } from 'dayjs';
import { Prica } from '../../prica/prica.model';
import { KomentarNaPricuService } from '../../komentar-na-pricu/service/komentar-na-pricu.service';
import { ASC, DESC } from '../../../config/pagination.constants';
import { AccountService } from '../../../core/auth/account.service';

@Component({
  selector: 'jhi-konacna-prica-detail',
  templateUrl: './konacna-prica-detail.component.html',
})
export class KonacnaPricaDetailComponent implements OnInit {
  konacnaPrica: IKonacnaPrica | null = null;
  idPrice?: number;
  isSaving = false;
  komentarNaPricus?: IKomentarNaPricu[];
  editForm = this.fb.group({
    tekstKomentara: [],
  });

  constructor(
    protected komentarNaPricuService: KomentarNaPricuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.idPrice = Number(localStorage.getItem('idPrice'));
    this.activatedRoute.data.subscribe(({ konacnaPrica }) => {
      this.konacnaPrica = konacnaPrica;
    });
    this.pozovi();
  }
  pozovi(): void {
    this.komentarNaPricuService.queryByIdPrica({ page: 1, size: 10, sort: ['ASC'] }, localStorage.getItem('idPrice')!).subscribe({
      next: (res: any) => this.onGetSucces(res.body),
      error: () => this.onSaveError(),
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
    let potpis = '';
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        potpis = String(account.firstName) + ' ' + String(account.lastName);
      }
    });
    return {
      ...new KomentarNaPricu(),
      tekst: String(this.editForm.get(['tekstKomentara'])!.value) + ' ' + String(potpis),
      datum: null,
      prica: new Prica(this.idPrice),
    };
  }

  private onGetSucces(data: IKomentarNaPricu[] | null): void {
    // eslint-disable-next-line no-console
    console.log('a', data);
    this.komentarNaPricus = data ?? [];
  }
}
