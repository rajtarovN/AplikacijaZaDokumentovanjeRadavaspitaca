import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKomentarNaPricu, KomentarNaPricu } from '../komentar-na-pricu.model';
import { KomentarNaPricuService } from '../service/komentar-na-pricu.service';

@Injectable({ providedIn: 'root' })
export class KomentarNaPricuRoutingResolveService implements Resolve<IKomentarNaPricu> {
  constructor(protected service: KomentarNaPricuService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKomentarNaPricu> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((komentarNaPricu: HttpResponse<KomentarNaPricu>) => {
          if (komentarNaPricu.body) {
            return of(komentarNaPricu.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new KomentarNaPricu());
  }
}
