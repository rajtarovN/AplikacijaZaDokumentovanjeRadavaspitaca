import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IObjekat, getObjekatIdentifier, ObjekatDTO } from '../objekat.model';

export type EntityResponseType = HttpResponse<IObjekat>;
export type EntityArrayResponseType = HttpResponse<IObjekat[]>;

@Injectable({ providedIn: 'root' })
export class ObjekatService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/objekats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(objekat: IObjekat): Observable<EntityResponseType> {
    return this.http.post<IObjekat>(this.resourceUrl, objekat, { observe: 'response' });
  }

  update(objekat: IObjekat): Observable<EntityResponseType> {
    return this.http.put<IObjekat>(`${this.resourceUrl}/${getObjekatIdentifier(objekat) as number}`, objekat, { observe: 'response' });
  }

  partialUpdate(objekat: IObjekat): Observable<EntityResponseType> {
    return this.http.patch<IObjekat>(`${this.resourceUrl}/${getObjekatIdentifier(objekat) as number}`, objekat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IObjekat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IObjekat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addObjekatToCollectionIfMissing(objekatCollection: IObjekat[], ...objekatsToCheck: (IObjekat | null | undefined)[]): IObjekat[] {
    const objekats: IObjekat[] = objekatsToCheck.filter(isPresent);
    if (objekats.length > 0) {
      const objekatCollectionIdentifiers = objekatCollection.map(objekatItem => getObjekatIdentifier(objekatItem)!);
      const objekatsToAdd = objekats.filter(objekatItem => {
        const objekatIdentifier = getObjekatIdentifier(objekatItem);
        if (objekatIdentifier == null || objekatCollectionIdentifiers.includes(objekatIdentifier)) {
          return false;
        }
        objekatCollectionIdentifiers.push(objekatIdentifier);
        return true;
      });
      return [...objekatsToAdd, ...objekatCollection];
    }
    return objekatCollection;
  }

  getAll(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ObjekatDTO[]>(this.resourceUrl + '/all', { params: options, observe: 'response' });
  }
}
