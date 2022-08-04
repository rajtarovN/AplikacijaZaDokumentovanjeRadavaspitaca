import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrica, getPricaIdentifier } from '../prica.model';

export type EntityResponseType = HttpResponse<IPrica>;
export type EntityArrayResponseType = HttpResponse<IPrica[]>;

@Injectable({ providedIn: 'root' })
export class PricaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pricas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prica: IPrica): Observable<EntityResponseType> {
    return this.http.post<IPrica>(this.resourceUrl, prica, { observe: 'response' });
  }

  update(prica: IPrica): Observable<EntityResponseType> {
    return this.http.put<IPrica>(`${this.resourceUrl}/${getPricaIdentifier(prica) as number}`, prica, { observe: 'response' });
  }

  partialUpdate(prica: IPrica): Observable<EntityResponseType> {
    return this.http.patch<IPrica>(`${this.resourceUrl}/${getPricaIdentifier(prica) as number}`, prica, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrica>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrica[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPricaToCollectionIfMissing(pricaCollection: IPrica[], ...pricasToCheck: (IPrica | null | undefined)[]): IPrica[] {
    const pricas: IPrica[] = pricasToCheck.filter(isPresent);
    if (pricas.length > 0) {
      const pricaCollectionIdentifiers = pricaCollection.map(pricaItem => getPricaIdentifier(pricaItem)!);
      const pricasToAdd = pricas.filter(pricaItem => {
        const pricaIdentifier = getPricaIdentifier(pricaItem);
        if (pricaIdentifier == null || pricaCollectionIdentifiers.includes(pricaIdentifier)) {
          return false;
        }
        pricaCollectionIdentifiers.push(pricaIdentifier);
        return true;
      });
      return [...pricasToAdd, ...pricaCollection];
    }
    return pricaCollection;
  }

  queryCurrentDnevnik(req: any, username: string): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrica[]>(this.resourceUrl + '/by/' + username, { params: options, observe: 'response' });
  }

  queryOldDnevnik(req: { size: number; page: number; sort: string[] }, dnevnik: string): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrica[]>(this.resourceUrl + '/byDnevnik/' + dnevnik, { params: options, observe: 'response' });
  }
}
