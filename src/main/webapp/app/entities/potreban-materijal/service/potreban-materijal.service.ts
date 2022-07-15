import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPotrebanMaterijal, getPotrebanMaterijalIdentifier } from '../potreban-materijal.model';

export type EntityResponseType = HttpResponse<IPotrebanMaterijal>;
export type EntityArrayResponseType = HttpResponse<IPotrebanMaterijal[]>;

@Injectable({ providedIn: 'root' })
export class PotrebanMaterijalService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/potreban-materijals');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(potrebanMaterijal: IPotrebanMaterijal): Observable<EntityResponseType> {
    return this.http.post<IPotrebanMaterijal>(this.resourceUrl, potrebanMaterijal, { observe: 'response' });
  }

  update(potrebanMaterijal: IPotrebanMaterijal): Observable<EntityResponseType> {
    return this.http.put<IPotrebanMaterijal>(
      `${this.resourceUrl}/${getPotrebanMaterijalIdentifier(potrebanMaterijal) as number}`,
      potrebanMaterijal,
      { observe: 'response' }
    );
  }

  partialUpdate(potrebanMaterijal: IPotrebanMaterijal): Observable<EntityResponseType> {
    return this.http.patch<IPotrebanMaterijal>(
      `${this.resourceUrl}/${getPotrebanMaterijalIdentifier(potrebanMaterijal) as number}`,
      potrebanMaterijal,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPotrebanMaterijal>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPotrebanMaterijal[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPotrebanMaterijalToCollectionIfMissing(
    potrebanMaterijalCollection: IPotrebanMaterijal[],
    ...potrebanMaterijalsToCheck: (IPotrebanMaterijal | null | undefined)[]
  ): IPotrebanMaterijal[] {
    const potrebanMaterijals: IPotrebanMaterijal[] = potrebanMaterijalsToCheck.filter(isPresent);
    if (potrebanMaterijals.length > 0) {
      const potrebanMaterijalCollectionIdentifiers = potrebanMaterijalCollection.map(
        potrebanMaterijalItem => getPotrebanMaterijalIdentifier(potrebanMaterijalItem)!
      );
      const potrebanMaterijalsToAdd = potrebanMaterijals.filter(potrebanMaterijalItem => {
        const potrebanMaterijalIdentifier = getPotrebanMaterijalIdentifier(potrebanMaterijalItem);
        if (potrebanMaterijalIdentifier == null || potrebanMaterijalCollectionIdentifiers.includes(potrebanMaterijalIdentifier)) {
          return false;
        }
        potrebanMaterijalCollectionIdentifiers.push(potrebanMaterijalIdentifier);
        return true;
      });
      return [...potrebanMaterijalsToAdd, ...potrebanMaterijalCollection];
    }
    return potrebanMaterijalCollection;
  }
}
