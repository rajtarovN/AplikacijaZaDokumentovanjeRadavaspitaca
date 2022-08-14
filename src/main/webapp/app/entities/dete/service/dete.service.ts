import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDete, getDeteIdentifier, ProfilDetetaDTO } from '../dete.model';

export type EntityResponseType = HttpResponse<IDete>;
export type EntityArrayResponseType = HttpResponse<IDete[]>;

@Injectable({ providedIn: 'root' })
export class DeteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/detes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dete: IDete): Observable<EntityResponseType> {
    return this.http.post<IDete>(this.resourceUrl, dete, { observe: 'response' });
  }

  update(dete: IDete): Observable<EntityResponseType> {
    return this.http.put<IDete>(`${this.resourceUrl}/${getDeteIdentifier(dete) as number}`, dete, { observe: 'response' });
  }

  partialUpdate(dete: IDete): Observable<EntityResponseType> {
    return this.http.patch<IDete>(`${this.resourceUrl}/${getDeteIdentifier(dete) as number}`, dete, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDete>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDete[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
  queryByGrupa(req?: any, grupa?: string): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDete[]>(this.resourceUrl + '/findByGrupa/' + String(grupa), { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeteToCollectionIfMissing(deteCollection: IDete[], ...detesToCheck: (IDete | null | undefined)[]): IDete[] {
    const detes: IDete[] = detesToCheck.filter(isPresent);
    if (detes.length > 0) {
      const deteCollectionIdentifiers = deteCollection.map(deteItem => getDeteIdentifier(deteItem)!);
      const detesToAdd = detes.filter(deteItem => {
        const deteIdentifier = getDeteIdentifier(deteItem);
        if (deteIdentifier == null || deteCollectionIdentifiers.includes(deteIdentifier)) {
          return false;
        }
        deteCollectionIdentifiers.push(deteIdentifier);
        return true;
      });
      return [...detesToAdd, ...deteCollection];
    }
    return deteCollection;
  }

  getprofile(id: number): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}/profil/${id}`, { observe: 'response' });
  }

  queryDeteOfRoditelj(
    req: {
      size: number;
      page: number;
      sort: string[];
    },
    username: string
  ): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDete[]>(this.resourceUrl + '/findByRoditelj/' + username, { params: options, observe: 'response' });
  }

  queryDecaOfVaspitac(
    req: { size: number; page: number; sort: string[]; username: string | undefined },
    vaspitac: string
  ): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDete[]>(this.resourceUrl + '/findForVaspitac/' + vaspitac, { params: options, observe: 'response' });
  }
}
