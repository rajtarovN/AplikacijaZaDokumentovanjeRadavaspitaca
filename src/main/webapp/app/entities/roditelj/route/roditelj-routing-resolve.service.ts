import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoditelj, Roditelj } from '../roditelj.model';
import { RoditeljService } from '../service/roditelj.service';

@Injectable({ providedIn: 'root' })
export class RoditeljRoutingResolveService implements Resolve<IRoditelj> {
  constructor(protected service: RoditeljService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRoditelj> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((roditelj: HttpResponse<Roditelj>) => {
          if (roditelj.body) {
            return of(roditelj.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Roditelj());
  }
}
