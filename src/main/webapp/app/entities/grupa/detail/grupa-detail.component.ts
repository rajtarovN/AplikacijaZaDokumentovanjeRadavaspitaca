import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGrupa } from '../grupa.model';

@Component({
  selector: 'jhi-grupa-detail',
  templateUrl: './grupa-detail.component.html',
})
export class GrupaDetailComponent implements OnInit {
  grupa: IGrupa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ grupa }) => {
      this.grupa = grupa;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
