import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDirektor, Direktor } from '../direktor.model';
import { DirektorService } from '../service/direktor.service';

@Injectable({ providedIn: 'root' })
export class DirektorRoutingResolveService implements Resolve<IDirektor> {
  constructor(protected service: DirektorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDirektor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((direktor: HttpResponse<Direktor>) => {
          if (direktor.body) {
            return of(direktor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Direktor());
  }
}
