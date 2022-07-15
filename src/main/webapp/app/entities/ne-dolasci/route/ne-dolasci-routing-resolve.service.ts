import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INeDolasci, NeDolasci } from '../ne-dolasci.model';
import { NeDolasciService } from '../service/ne-dolasci.service';

@Injectable({ providedIn: 'root' })
export class NeDolasciRoutingResolveService implements Resolve<INeDolasci> {
  constructor(protected service: NeDolasciService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INeDolasci> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((neDolasci: HttpResponse<NeDolasci>) => {
          if (neDolasci.body) {
            return of(neDolasci.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NeDolasci());
  }
}
