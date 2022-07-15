import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IObjekat } from '../objekat.model';

@Component({
  selector: 'jhi-objekat-detail',
  templateUrl: './objekat-detail.component.html',
})
export class ObjekatDetailComponent implements OnInit {
  objekat: IObjekat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ objekat }) => {
      this.objekat = objekat;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
