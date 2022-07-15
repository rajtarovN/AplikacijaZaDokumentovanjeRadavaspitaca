import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdresa, getAdresaIdentifier } from '../adresa.model';

export type EntityResponseType = HttpResponse<IAdresa>;
export type EntityArrayResponseType = HttpResponse<IAdresa[]>;

@Injectable({ providedIn: 'root' })
export class AdresaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/adresas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(adresa: IAdresa): Observable<EntityResponseType> {
    return this.http.post<IAdresa>(this.resourceUrl, adresa, { observe: 'response' });
  }

  update(adresa: IAdresa): Observable<EntityResponseType> {
    return this.http.put<IAdresa>(`${this.resourceUrl}/${getAdresaIdentifier(adresa) as number}`, adresa, { observe: 'response' });
  }

  partialUpdate(adresa: IAdresa): Observable<EntityResponseType> {
    return this.http.patch<IAdresa>(`${this.resourceUrl}/${getAdresaIdentifier(adresa) as number}`, adresa, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdresa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdresa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAdresaToCollectionIfMissing(adresaCollection: IAdresa[], ...adresasToCheck: (IAdresa | null | undefined)[]): IAdresa[] {
    const adresas: IAdresa[] = adresasToCheck.filter(isPresent);
    if (adresas.length > 0) {
      const adresaCollectionIdentifiers = adresaCollection.map(adresaItem => getAdresaIdentifier(adresaItem)!);
      const adresasToAdd = adresas.filter(adresaItem => {
        const adresaIdentifier = getAdresaIdentifier(adresaItem);
        if (adresaIdentifier == null || adresaCollectionIdentifiers.includes(adresaIdentifier)) {
          return false;
        }
        adresaCollectionIdentifiers.push(adresaIdentifier);
        return true;
      });
      return [...adresasToAdd, ...adresaCollection];
    }
    return adresaCollection;
  }
}
