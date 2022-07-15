import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKorisnik, Korisnik } from '../korisnik.model';
import { KorisnikService } from '../service/korisnik.service';

@Injectable({ providedIn: 'root' })
export class KorisnikRoutingResolveService implements Resolve<IKorisnik> {
  constructor(protected service: KorisnikService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKorisnik> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((korisnik: HttpResponse<Korisnik>) => {
          if (korisnik.body) {
            return of(korisnik.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Korisnik());
  }
}
