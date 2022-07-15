import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVaspitac, Vaspitac } from '../vaspitac.model';
import { VaspitacService } from '../service/vaspitac.service';

@Injectable({ providedIn: 'root' })
export class VaspitacRoutingResolveService implements Resolve<IVaspitac> {
  constructor(protected service: VaspitacService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVaspitac> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vaspitac: HttpResponse<Vaspitac>) => {
          if (vaspitac.body) {
            return of(vaspitac.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vaspitac());
  }
}
