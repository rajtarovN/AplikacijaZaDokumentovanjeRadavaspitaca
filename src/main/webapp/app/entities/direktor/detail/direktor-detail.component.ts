import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDirektor } from '../direktor.model';

@Component({
  selector: 'jhi-direktor-detail',
  templateUrl: './direktor-detail.component.html',
})
export class DirektorDetailComponent implements OnInit {
  direktor: IDirektor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ direktor }) => {
      this.direktor = direktor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
