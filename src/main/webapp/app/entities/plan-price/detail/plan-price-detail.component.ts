import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlanPrice } from '../plan-price.model';

@Component({
  selector: 'jhi-plan-price-detail',
  templateUrl: './plan-price-detail.component.html',
})
export class PlanPriceDetailComponent implements OnInit {
  planPrice: IPlanPrice | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planPrice }) => {
      this.planPrice = planPrice;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
