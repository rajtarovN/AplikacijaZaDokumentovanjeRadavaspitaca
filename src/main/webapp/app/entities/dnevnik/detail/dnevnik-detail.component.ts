import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDnevnik } from '../dnevnik.model';

@Component({
  selector: 'jhi-dnevnik-detail',
  templateUrl: './dnevnik-detail.component.html',
})
export class DnevnikDetailComponent implements OnInit {
  dnevnik: IDnevnik | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dnevnik }) => {
      this.dnevnik = dnevnik;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
