import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPedagog, Pedagog } from '../pedagog.model';
import { PedagogService } from '../service/pedagog.service';

@Injectable({ providedIn: 'root' })
export class PedagogRoutingResolveService implements Resolve<IPedagog> {
  constructor(protected service: PedagogService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPedagog> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pedagog: HttpResponse<Pedagog>) => {
          if (pedagog.body) {
            return of(pedagog.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Pedagog());
  }
}
