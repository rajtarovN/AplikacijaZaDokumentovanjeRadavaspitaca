import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDnevnik, Dnevnik } from '../dnevnik.model';
import { DnevnikService } from '../service/dnevnik.service';

@Injectable({ providedIn: 'root' })
export class DnevnikRoutingResolveService implements Resolve<IDnevnik> {
  constructor(protected service: DnevnikService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDnevnik> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dnevnik: HttpResponse<Dnevnik>) => {
          if (dnevnik.body) {
            return of(dnevnik.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dnevnik());
  }
}
