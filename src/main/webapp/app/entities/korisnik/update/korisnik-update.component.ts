import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IKorisnik, Korisnik } from '../korisnik.model';
import { KorisnikService } from '../service/korisnik.service';

@Component({
  selector: 'jhi-korisnik-update',
  templateUrl: './korisnik-update.component.html',
})
export class KorisnikUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    korisnickoIme: [],
    sifra: [],
    ime: [],
    prezime: [],
    id: [],
  });

  constructor(protected korisnikService: KorisnikService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ korisnik }) => {
      this.updateForm(korisnik);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const korisnik = this.createFromForm();
    if (korisnik.id !== undefined) {
      this.subscribeToSaveResponse(this.korisnikService.update(korisnik));
    } else {
      this.subscribeToSaveResponse(this.korisnikService.create(korisnik));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKorisnik>>): void {
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

  protected updateForm(korisnik: IKorisnik): void {
    this.editForm.patchValue({
      korisnickoIme: korisnik.korisnickoIme,
      sifra: korisnik.sifra,
      ime: korisnik.ime,
      prezime: korisnik.prezime,
      id: korisnik.id,
    });
  }

  protected createFromForm(): IKorisnik {
    return {
      ...new Korisnik(),
      korisnickoIme: this.editForm.get(['korisnickoIme'])!.value,
      sifra: this.editForm.get(['sifra'])!.value,
      ime: this.editForm.get(['ime'])!.value,
      prezime: this.editForm.get(['prezime'])!.value,
      id: this.editForm.get(['id'])!.value,
    };
  }
}
