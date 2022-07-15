import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrica, Prica } from '../prica.model';
import { PricaService } from '../service/prica.service';

@Injectable({ providedIn: 'root' })
export class PricaRoutingResolveService implements Resolve<IPrica> {
  constructor(protected service: PricaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrica> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prica: HttpResponse<Prica>) => {
          if (prica.body) {
            return of(prica.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Prica());
  }
}
