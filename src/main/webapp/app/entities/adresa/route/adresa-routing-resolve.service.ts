import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdresa, Adresa } from '../adresa.model';
import { AdresaService } from '../service/adresa.service';

@Injectable({ providedIn: 'root' })
export class AdresaRoutingResolveService implements Resolve<IAdresa> {
  constructor(protected service: AdresaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdresa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((adresa: HttpResponse<Adresa>) => {
          if (adresa.body) {
            return of(adresa.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Adresa());
  }
}
