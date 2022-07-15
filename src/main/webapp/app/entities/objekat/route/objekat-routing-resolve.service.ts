import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IObjekat, Objekat } from '../objekat.model';
import { ObjekatService } from '../service/objekat.service';

@Injectable({ providedIn: 'root' })
export class ObjekatRoutingResolveService implements Resolve<IObjekat> {
  constructor(protected service: ObjekatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IObjekat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((objekat: HttpResponse<Objekat>) => {
          if (objekat.body) {
            return of(objekat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Objekat());
  }
}
