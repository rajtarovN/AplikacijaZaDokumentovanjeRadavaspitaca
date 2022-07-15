import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormular } from '../formular.model';

@Component({
  selector: 'jhi-formular-detail',
  templateUrl: './formular-detail.component.html',
})
export class FormularDetailComponent implements OnInit {
  formular: IFormular | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formular }) => {
      this.formular = formular;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
