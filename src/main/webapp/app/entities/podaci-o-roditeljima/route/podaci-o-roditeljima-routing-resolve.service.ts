import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPodaciORoditeljima, PodaciORoditeljima } from '../podaci-o-roditeljima.model';
import { PodaciORoditeljimaService } from '../service/podaci-o-roditeljima.service';

@Injectable({ providedIn: 'root' })
export class PodaciORoditeljimaRoutingResolveService implements Resolve<IPodaciORoditeljima> {
  constructor(protected service: PodaciORoditeljimaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPodaciORoditeljima> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((podaciORoditeljima: HttpResponse<PodaciORoditeljima>) => {
          if (podaciORoditeljima.body) {
            return of(podaciORoditeljima.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PodaciORoditeljima());
  }
}
