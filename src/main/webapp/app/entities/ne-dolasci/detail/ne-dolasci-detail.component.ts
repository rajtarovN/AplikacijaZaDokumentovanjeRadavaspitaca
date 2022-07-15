import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INeDolasci } from '../ne-dolasci.model';

@Component({
  selector: 'jhi-ne-dolasci-detail',
  templateUrl: './ne-dolasci-detail.component.html',
})
export class NeDolasciDetailComponent implements OnInit {
  neDolasci: INeDolasci | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ neDolasci }) => {
      this.neDolasci = neDolasci;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
