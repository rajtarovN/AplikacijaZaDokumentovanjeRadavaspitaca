import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPotrebanMaterijal, PotrebanMaterijal } from '../potreban-materijal.model';
import { PotrebanMaterijalService } from '../service/potreban-materijal.service';

@Injectable({ providedIn: 'root' })
export class PotrebanMaterijalRoutingResolveService implements Resolve<IPotrebanMaterijal> {
  constructor(protected service: PotrebanMaterijalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPotrebanMaterijal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((potrebanMaterijal: HttpResponse<PotrebanMaterijal>) => {
          if (potrebanMaterijal.body) {
            return of(potrebanMaterijal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PotrebanMaterijal());
  }
}
