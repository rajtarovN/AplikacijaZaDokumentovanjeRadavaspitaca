import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPotrebanMaterijal } from '../potreban-materijal.model';

@Component({
  selector: 'jhi-potreban-materijal-detail',
  templateUrl: './potreban-materijal-detail.component.html',
})
export class PotrebanMaterijalDetailComponent implements OnInit {
  potrebanMaterijal: IPotrebanMaterijal | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ potrebanMaterijal }) => {
      this.potrebanMaterijal = potrebanMaterijal;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
