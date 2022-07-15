import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKonacnaPrica, KonacnaPrica } from '../konacna-prica.model';
import { KonacnaPricaService } from '../service/konacna-prica.service';

@Injectable({ providedIn: 'root' })
export class KonacnaPricaRoutingResolveService implements Resolve<IKonacnaPrica> {
  constructor(protected service: KonacnaPricaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKonacnaPrica> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((konacnaPrica: HttpResponse<KonacnaPrica>) => {
          if (konacnaPrica.body) {
            return of(konacnaPrica.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new KonacnaPrica());
  }
}
