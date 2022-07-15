import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKomentarNaPricu } from '../komentar-na-pricu.model';

@Component({
  selector: 'jhi-komentar-na-pricu-detail',
  templateUrl: './komentar-na-pricu-detail.component.html',
})
export class KomentarNaPricuDetailComponent implements OnInit {
  komentarNaPricu: IKomentarNaPricu | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ komentarNaPricu }) => {
      this.komentarNaPricu = komentarNaPricu;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
