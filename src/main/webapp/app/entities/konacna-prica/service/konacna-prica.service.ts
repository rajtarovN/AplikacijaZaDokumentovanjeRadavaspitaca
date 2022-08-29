import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IKonacnaPrica, getKonacnaPricaIdentifier } from '../konacna-prica.model';

export type EntityResponseType = HttpResponse<IKonacnaPrica>;
export type EntityArrayResponseType = HttpResponse<IKonacnaPrica[]>;

@Injectable({ providedIn: 'root' })
export class KonacnaPricaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/konacna-pricas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(konacnaPrica: IKonacnaPrica, pricaId?: string | null): Observable<EntityResponseType> {
    return this.http.post<IKonacnaPrica>(this.resourceUrl + '/' + pricaId!, konacnaPrica, { observe: 'response' });
  }

  update(konacnaPrica: IKonacnaPrica): Observable<EntityResponseType> {
    return this.http.put<IKonacnaPrica>(`${this.resourceUrl}/${getKonacnaPricaIdentifier(konacnaPrica) as number}`, konacnaPrica, {
      observe: 'response',
    });
  }

  partialUpdate(konacnaPrica: IKonacnaPrica): Observable<EntityResponseType> {
    return this.http.patch<IKonacnaPrica>(`${this.resourceUrl}/${getKonacnaPricaIdentifier(konacnaPrica) as number}`, konacnaPrica, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IKonacnaPrica>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKonacnaPrica[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addKonacnaPricaToCollectionIfMissing(
    konacnaPricaCollection: IKonacnaPrica[],
    ...konacnaPricasToCheck: (IKonacnaPrica | null | undefined)[]
  ): IKonacnaPrica[] {
    const konacnaPricas: IKonacnaPrica[] = konacnaPricasToCheck.filter(isPresent);
    if (konacnaPricas.length > 0) {
      const konacnaPricaCollectionIdentifiers = konacnaPricaCollection.map(
        konacnaPricaItem => getKonacnaPricaIdentifier(konacnaPricaItem)!
      );
      const konacnaPricasToAdd = konacnaPricas.filter(konacnaPricaItem => {
        const konacnaPricaIdentifier = getKonacnaPricaIdentifier(konacnaPricaItem);
        if (konacnaPricaIdentifier == null || konacnaPricaCollectionIdentifiers.includes(konacnaPricaIdentifier)) {
          return false;
        }
        konacnaPricaCollectionIdentifiers.push(konacnaPricaIdentifier);
        return true;
      });
      return [...konacnaPricasToAdd, ...konacnaPricaCollection];
    }
    return konacnaPricaCollection;
  }

  getPocetnaPrica(id: string): any {
    return this.http.get<IKonacnaPrica>(`${this.resourceUrl}/getPocetnaPrica/${id}`, { observe: 'response' });
  }
}
