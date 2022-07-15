import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGrupa, getGrupaIdentifier } from '../grupa.model';

export type EntityResponseType = HttpResponse<IGrupa>;
export type EntityArrayResponseType = HttpResponse<IGrupa[]>;

@Injectable({ providedIn: 'root' })
export class GrupaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/grupas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(grupa: IGrupa): Observable<EntityResponseType> {
    return this.http.post<IGrupa>(this.resourceUrl, grupa, { observe: 'response' });
  }

  update(grupa: IGrupa): Observable<EntityResponseType> {
    return this.http.put<IGrupa>(`${this.resourceUrl}/${getGrupaIdentifier(grupa) as number}`, grupa, { observe: 'response' });
  }

  partialUpdate(grupa: IGrupa): Observable<EntityResponseType> {
    return this.http.patch<IGrupa>(`${this.resourceUrl}/${getGrupaIdentifier(grupa) as number}`, grupa, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGrupa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGrupa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGrupaToCollectionIfMissing(grupaCollection: IGrupa[], ...grupasToCheck: (IGrupa | null | undefined)[]): IGrupa[] {
    const grupas: IGrupa[] = grupasToCheck.filter(isPresent);
    if (grupas.length > 0) {
      const grupaCollectionIdentifiers = grupaCollection.map(grupaItem => getGrupaIdentifier(grupaItem)!);
      const grupasToAdd = grupas.filter(grupaItem => {
        const grupaIdentifier = getGrupaIdentifier(grupaItem);
        if (grupaIdentifier == null || grupaCollectionIdentifiers.includes(grupaIdentifier)) {
          return false;
        }
        grupaCollectionIdentifiers.push(grupaIdentifier);
        return true;
      });
      return [...grupasToAdd, ...grupaCollection];
    }
    return grupaCollection;
  }
}
