import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKonacnaPrica } from '../konacna-prica.model';

@Component({
  selector: 'jhi-konacna-prica-detail',
  templateUrl: './konacna-prica-detail.component.html',
})
export class KonacnaPricaDetailComponent implements OnInit {
  konacnaPrica: IKonacnaPrica | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ konacnaPrica }) => {
      this.konacnaPrica = konacnaPrica;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
