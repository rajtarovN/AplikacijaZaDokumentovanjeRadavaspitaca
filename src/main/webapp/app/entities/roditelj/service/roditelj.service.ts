import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoditelj, getRoditeljIdentifier } from '../roditelj.model';

export type EntityResponseType = HttpResponse<IRoditelj>;
export type EntityArrayResponseType = HttpResponse<IRoditelj[]>;

@Injectable({ providedIn: 'root' })
export class RoditeljService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/roditeljs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(roditelj: IRoditelj): Observable<EntityResponseType> {
    return this.http.post<IRoditelj>(this.resourceUrl, roditelj, { observe: 'response' });
  }

  update(roditelj: IRoditelj): Observable<EntityResponseType> {
    return this.http.put<IRoditelj>(`${this.resourceUrl}/${getRoditeljIdentifier(roditelj) as number}`, roditelj, { observe: 'response' });
  }

  partialUpdate(roditelj: IRoditelj): Observable<EntityResponseType> {
    return this.http.patch<IRoditelj>(`${this.resourceUrl}/${getRoditeljIdentifier(roditelj) as number}`, roditelj, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRoditelj>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoditelj[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRoditeljToCollectionIfMissing(roditeljCollection: IRoditelj[], ...roditeljsToCheck: (IRoditelj | null | undefined)[]): IRoditelj[] {
    const roditeljs: IRoditelj[] = roditeljsToCheck.filter(isPresent);
    if (roditeljs.length > 0) {
      const roditeljCollectionIdentifiers = roditeljCollection.map(roditeljItem => getRoditeljIdentifier(roditeljItem)!);
      const roditeljsToAdd = roditeljs.filter(roditeljItem => {
        const roditeljIdentifier = getRoditeljIdentifier(roditeljItem);
        if (roditeljIdentifier == null || roditeljCollectionIdentifiers.includes(roditeljIdentifier)) {
          return false;
        }
        roditeljCollectionIdentifiers.push(roditeljIdentifier);
        return true;
      });
      return [...roditeljsToAdd, ...roditeljCollection];
    }
    return roditeljCollection;
  }
}
