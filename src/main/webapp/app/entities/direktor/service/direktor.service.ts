import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDirektor, getDirektorIdentifier } from '../direktor.model';
import { IUser } from '../../../admin/user-management/user-management.model';

export type EntityResponseType = HttpResponse<IDirektor>;
export type EntityArrayResponseType = HttpResponse<IDirektor[]>;

@Injectable({ providedIn: 'root' })
export class DirektorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/direktors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(direktor: IDirektor): Observable<EntityResponseType> {
    return this.http.post<IDirektor>(this.resourceUrl, direktor, { observe: 'response' });
  }

  update(direktor: IDirektor): Observable<EntityResponseType> {
    return this.http.put<IDirektor>(`${this.resourceUrl}/${getDirektorIdentifier(direktor) as number}`, direktor, { observe: 'response' });
  }

  partialUpdate(direktor: IDirektor): Observable<EntityResponseType> {
    return this.http.patch<IDirektor>(`${this.resourceUrl}/${getDirektorIdentifier(direktor) as number}`, direktor, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDirektor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDirektor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDirektorToCollectionIfMissing(direktorCollection: IDirektor[], ...direktorsToCheck: (IDirektor | null | undefined)[]): IDirektor[] {
    const direktors: IDirektor[] = direktorsToCheck.filter(isPresent);
    if (direktors.length > 0) {
      const direktorCollectionIdentifiers = direktorCollection.map(direktorItem => getDirektorIdentifier(direktorItem)!);
      const direktorsToAdd = direktors.filter(direktorItem => {
        const direktorIdentifier = getDirektorIdentifier(direktorItem);
        if (direktorIdentifier == null || direktorCollectionIdentifiers.includes(direktorIdentifier)) {
          return false;
        }
        direktorCollectionIdentifiers.push(direktorIdentifier);
        return true;
      });
      return [...direktorsToAdd, ...direktorCollection];
    }
    return direktorCollection;
  }
  createZaposlen(user: IUser): Observable<IUser> {
    return this.http.post<IUser>('api/createDirektor', user);
  }
}
