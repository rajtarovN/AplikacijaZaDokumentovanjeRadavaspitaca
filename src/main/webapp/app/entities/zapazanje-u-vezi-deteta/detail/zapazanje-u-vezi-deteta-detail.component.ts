import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IZapazanjeUVeziDeteta } from '../zapazanje-u-vezi-deteta.model';

@Component({
  selector: 'jhi-zapazanje-u-vezi-deteta-detail',
  templateUrl: './zapazanje-u-vezi-deteta-detail.component.html',
})
export class ZapazanjeUVeziDetetaDetailComponent implements OnInit {
  zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zapazanjeUVeziDeteta }) => {
      this.zapazanjeUVeziDeteta = zapazanjeUVeziDeteta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
