import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrica } from '../prica.model';

@Component({
  selector: 'jhi-prica-detail',
  templateUrl: './prica-detail.component.html',
})
export class PricaDetailComponent implements OnInit {
  prica: IPrica | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prica }) => {
      this.prica = prica;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
