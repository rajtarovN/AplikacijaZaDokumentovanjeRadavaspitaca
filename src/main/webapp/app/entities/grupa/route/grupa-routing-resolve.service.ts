import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGrupa, Grupa } from '../grupa.model';
import { GrupaService } from '../service/grupa.service';

@Injectable({ providedIn: 'root' })
export class GrupaRoutingResolveService implements Resolve<IGrupa> {
  constructor(protected service: GrupaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGrupa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((grupa: HttpResponse<Grupa>) => {
          if (grupa.body) {
            return of(grupa.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Grupa());
  }
}
