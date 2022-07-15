import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDete } from '../dete.model';

@Component({
  selector: 'jhi-dete-detail',
  templateUrl: './dete-detail.component.html',
})
export class DeteDetailComponent implements OnInit {
  dete: IDete | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dete }) => {
      this.dete = dete;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
