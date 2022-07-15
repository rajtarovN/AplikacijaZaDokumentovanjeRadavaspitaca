import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDete, Dete } from '../dete.model';
import { DeteService } from '../service/dete.service';

@Injectable({ providedIn: 'root' })
export class DeteRoutingResolveService implements Resolve<IDete> {
  constructor(protected service: DeteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDete> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dete: HttpResponse<Dete>) => {
          if (dete.body) {
            return of(dete.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dete());
  }
}
