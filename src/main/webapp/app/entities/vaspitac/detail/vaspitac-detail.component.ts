import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVaspitac } from '../vaspitac.model';

@Component({
  selector: 'jhi-vaspitac-detail',
  templateUrl: './vaspitac-detail.component.html',
})
export class VaspitacDetailComponent implements OnInit {
  vaspitac: IVaspitac | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vaspitac }) => {
      this.vaspitac = vaspitac;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
