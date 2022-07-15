import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlanPrice, PlanPrice } from '../plan-price.model';
import { PlanPriceService } from '../service/plan-price.service';

@Injectable({ providedIn: 'root' })
export class PlanPriceRoutingResolveService implements Resolve<IPlanPrice> {
  constructor(protected service: PlanPriceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlanPrice> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((planPrice: HttpResponse<PlanPrice>) => {
          if (planPrice.body) {
            return of(planPrice.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PlanPrice());
  }
}
