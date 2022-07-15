import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdresa } from '../adresa.model';

@Component({
  selector: 'jhi-adresa-detail',
  templateUrl: './adresa-detail.component.html',
})
export class AdresaDetailComponent implements OnInit {
  adresa: IAdresa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adresa }) => {
      this.adresa = adresa;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
