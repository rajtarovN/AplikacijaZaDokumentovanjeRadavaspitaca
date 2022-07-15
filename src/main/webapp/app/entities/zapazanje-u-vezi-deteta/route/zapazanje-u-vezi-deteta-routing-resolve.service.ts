import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IZapazanjeUVeziDeteta, ZapazanjeUVeziDeteta } from '../zapazanje-u-vezi-deteta.model';
import { ZapazanjeUVeziDetetaService } from '../service/zapazanje-u-vezi-deteta.service';

@Injectable({ providedIn: 'root' })
export class ZapazanjeUVeziDetetaRoutingResolveService implements Resolve<IZapazanjeUVeziDeteta> {
  constructor(protected service: ZapazanjeUVeziDetetaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IZapazanjeUVeziDeteta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((zapazanjeUVeziDeteta: HttpResponse<ZapazanjeUVeziDeteta>) => {
          if (zapazanjeUVeziDeteta.body) {
            return of(zapazanjeUVeziDeteta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ZapazanjeUVeziDeteta());
  }
}
