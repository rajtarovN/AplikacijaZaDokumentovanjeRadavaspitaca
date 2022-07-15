import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormular, Formular } from '../formular.model';
import { FormularService } from '../service/formular.service';

@Injectable({ providedIn: 'root' })
export class FormularRoutingResolveService implements Resolve<IFormular> {
  constructor(protected service: FormularService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFormular> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((formular: HttpResponse<Formular>) => {
          if (formular.body) {
            return of(formular.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Formular());
  }
}
