import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKorisnik } from '../korisnik.model';

@Component({
  selector: 'jhi-korisnik-detail',
  templateUrl: './korisnik-detail.component.html',
})
export class KorisnikDetailComponent implements OnInit {
  korisnik: IKorisnik | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ korisnik }) => {
      this.korisnik = korisnik;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
