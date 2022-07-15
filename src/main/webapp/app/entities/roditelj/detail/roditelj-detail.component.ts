import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoditelj } from '../roditelj.model';

@Component({
  selector: 'jhi-roditelj-detail',
  templateUrl: './roditelj-detail.component.html',
})
export class RoditeljDetailComponent implements OnInit {
  roditelj: IRoditelj | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roditelj }) => {
      this.roditelj = roditelj;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
