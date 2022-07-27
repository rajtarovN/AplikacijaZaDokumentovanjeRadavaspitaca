import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormular, getFormularIdentifier, DeteZaGrupuDTO } from '../formular.model';
import { IVaspitac } from '../../vaspitac/vaspitac.model';
import { map } from 'rxjs/operators';

export type EntityResponseType = HttpResponse<IFormular>;
export type EntityArrayResponseType = HttpResponse<IFormular[]>;

@Injectable({ providedIn: 'root' })
export class FormularService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/formulars');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(formular: IFormular): Observable<EntityResponseType> {
    return this.http.post<IFormular>(this.resourceUrl, formular, { observe: 'response' });
  }

  update(formular: IFormular): Observable<EntityResponseType> {
    return this.http.put<IFormular>(`${this.resourceUrl}/${getFormularIdentifier(formular) as number}`, formular, { observe: 'response' });
  }

  partialUpdate(formular: IFormular): Observable<EntityResponseType> {
    return this.http.patch<IFormular>(`${this.resourceUrl}/${getFormularIdentifier(formular) as number}`, formular, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFormular>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFormular[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFormularToCollectionIfMissing(formularCollection: IFormular[], ...formularsToCheck: (IFormular | null | undefined)[]): IFormular[] {
    const formulars: IFormular[] = formularsToCheck.filter(isPresent);
    if (formulars.length > 0) {
      const formularCollectionIdentifiers = formularCollection.map(formularItem => getFormularIdentifier(formularItem)!);
      const formularsToAdd = formulars.filter(formularItem => {
        const formularIdentifier = getFormularIdentifier(formularItem);
        if (formularIdentifier == null || formularCollectionIdentifiers.includes(formularIdentifier)) {
          return false;
        }
        formularCollectionIdentifiers.push(formularIdentifier);
        return true;
      });
      return [...formularsToAdd, ...formularCollection];
    }
    return formularCollection;
  }

  getDecaZaRaspored(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<DeteZaGrupuDTO[]>(this.resourceUrl + '/getDeca', { params: options, observe: 'response' });
  }
}
