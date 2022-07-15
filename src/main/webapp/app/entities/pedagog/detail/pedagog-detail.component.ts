import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPedagog } from '../pedagog.model';

@Component({
  selector: 'jhi-pedagog-detail',
  templateUrl: './pedagog-detail.component.html',
})
export class PedagogDetailComponent implements OnInit {
  pedagog: IPedagog | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pedagog }) => {
      this.pedagog = pedagog;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
