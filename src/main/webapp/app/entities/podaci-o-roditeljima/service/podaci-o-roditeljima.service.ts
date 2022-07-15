import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPodaciORoditeljima, getPodaciORoditeljimaIdentifier } from '../podaci-o-roditeljima.model';

export type EntityResponseType = HttpResponse<IPodaciORoditeljima>;
export type EntityArrayResponseType = HttpResponse<IPodaciORoditeljima[]>;

@Injectable({ providedIn: 'root' })
export class PodaciORoditeljimaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/podaci-o-roditeljimas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(podaciORoditeljima: IPodaciORoditeljima): Observable<EntityResponseType> {
    return this.http.post<IPodaciORoditeljima>(this.resourceUrl, podaciORoditeljima, { observe: 'response' });
  }

  update(podaciORoditeljima: IPodaciORoditeljima): Observable<EntityResponseType> {
    return this.http.put<IPodaciORoditeljima>(
      `${this.resourceUrl}/${getPodaciORoditeljimaIdentifier(podaciORoditeljima) as number}`,
      podaciORoditeljima,
      { observe: 'response' }
    );
  }

  partialUpdate(podaciORoditeljima: IPodaciORoditeljima): Observable<EntityResponseType> {
    return this.http.patch<IPodaciORoditeljima>(
      `${this.resourceUrl}/${getPodaciORoditeljimaIdentifier(podaciORoditeljima) as number}`,
      podaciORoditeljima,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPodaciORoditeljima>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPodaciORoditeljima[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPodaciORoditeljimaToCollectionIfMissing(
    podaciORoditeljimaCollection: IPodaciORoditeljima[],
    ...podaciORoditeljimasToCheck: (IPodaciORoditeljima | null | undefined)[]
  ): IPodaciORoditeljima[] {
    const podaciORoditeljimas: IPodaciORoditeljima[] = podaciORoditeljimasToCheck.filter(isPresent);
    if (podaciORoditeljimas.length > 0) {
      const podaciORoditeljimaCollectionIdentifiers = podaciORoditeljimaCollection.map(
        podaciORoditeljimaItem => getPodaciORoditeljimaIdentifier(podaciORoditeljimaItem)!
      );
      const podaciORoditeljimasToAdd = podaciORoditeljimas.filter(podaciORoditeljimaItem => {
        const podaciORoditeljimaIdentifier = getPodaciORoditeljimaIdentifier(podaciORoditeljimaItem);
        if (podaciORoditeljimaIdentifier == null || podaciORoditeljimaCollectionIdentifiers.includes(podaciORoditeljimaIdentifier)) {
          return false;
        }
        podaciORoditeljimaCollectionIdentifiers.push(podaciORoditeljimaIdentifier);
        return true;
      });
      return [...podaciORoditeljimasToAdd, ...podaciORoditeljimaCollection];
    }
    return podaciORoditeljimaCollection;
  }
}
