import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPodaciORoditeljima } from '../podaci-o-roditeljima.model';

@Component({
  selector: 'jhi-podaci-o-roditeljima-detail',
  templateUrl: './podaci-o-roditeljima-detail.component.html',
})
export class PodaciORoditeljimaDetailComponent implements OnInit {
  podaciORoditeljima: IPodaciORoditeljima | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ podaciORoditeljima }) => {
      this.podaciORoditeljima = podaciORoditeljima;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
