import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPedagog, getPedagogIdentifier } from '../pedagog.model';
import { IUser } from '../../../admin/user-management/user-management.model';

export type EntityResponseType = HttpResponse<IPedagog>;
export type EntityArrayResponseType = HttpResponse<IPedagog[]>;

@Injectable({ providedIn: 'root' })
export class PedagogService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pedagogs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pedagog: IPedagog): Observable<EntityResponseType> {
    return this.http.post<IPedagog>(this.resourceUrl, pedagog, { observe: 'response' });
  }

  update(pedagog: IPedagog): Observable<EntityResponseType> {
    return this.http.put<IPedagog>(`${this.resourceUrl}/${getPedagogIdentifier(pedagog) as number}`, pedagog, { observe: 'response' });
  }

  partialUpdate(pedagog: IPedagog): Observable<EntityResponseType> {
    return this.http.patch<IPedagog>(`${this.resourceUrl}/${getPedagogIdentifier(pedagog) as number}`, pedagog, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPedagog>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPedagog[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPedagogToCollectionIfMissing(pedagogCollection: IPedagog[], ...pedagogsToCheck: (IPedagog | null | undefined)[]): IPedagog[] {
    const pedagogs: IPedagog[] = pedagogsToCheck.filter(isPresent);
    if (pedagogs.length > 0) {
      const pedagogCollectionIdentifiers = pedagogCollection.map(pedagogItem => getPedagogIdentifier(pedagogItem)!);
      const pedagogsToAdd = pedagogs.filter(pedagogItem => {
        const pedagogIdentifier = getPedagogIdentifier(pedagogItem);
        if (pedagogIdentifier == null || pedagogCollectionIdentifiers.includes(pedagogIdentifier)) {
          return false;
        }
        pedagogCollectionIdentifiers.push(pedagogIdentifier);
        return true;
      });
      return [...pedagogsToAdd, ...pedagogCollection];
    }
    return pedagogCollection;
  }

  createZaposlen(user: IUser): Observable<IUser> {
    return this.http.post<IUser>('api/createPedagog', user);
  }
}
