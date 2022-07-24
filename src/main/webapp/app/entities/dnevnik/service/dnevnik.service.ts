import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDnevnik, getDnevnikIdentifier } from '../dnevnik.model';
import { DeteDTO } from '../../ne-dolasci/ne-dolasci.model';
import { ObjekatDTO } from '../../objekat/objekat.model';

export type EntityResponseType = HttpResponse<IDnevnik>;
export type EntityArrayResponseType = HttpResponse<IDnevnik[]>;

@Injectable({ providedIn: 'root' })
export class DnevnikService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dnevniks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dnevnik: IDnevnik): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dnevnik);
    return this.http
      .post<IDnevnik>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dnevnik: IDnevnik): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dnevnik);
    return this.http
      .put<IDnevnik>(`${this.resourceUrl}/${getDnevnikIdentifier(dnevnik) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(dnevnik: IDnevnik): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dnevnik);
    return this.http
      .patch<IDnevnik>(`${this.resourceUrl}/${getDnevnikIdentifier(dnevnik) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDnevnik>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDnevnik[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDnevnikToCollectionIfMissing(dnevnikCollection: IDnevnik[], ...dnevniksToCheck: (IDnevnik | null | undefined)[]): IDnevnik[] {
    const dnevniks: IDnevnik[] = dnevniksToCheck.filter(isPresent);
    if (dnevniks.length > 0) {
      const dnevnikCollectionIdentifiers = dnevnikCollection.map(dnevnikItem => getDnevnikIdentifier(dnevnikItem)!);
      const dnevniksToAdd = dnevniks.filter(dnevnikItem => {
        const dnevnikIdentifier = getDnevnikIdentifier(dnevnikItem);
        if (dnevnikIdentifier == null || dnevnikCollectionIdentifiers.includes(dnevnikIdentifier)) {
          return false;
        }
        dnevnikCollectionIdentifiers.push(dnevnikIdentifier);
        return true;
      });
      return [...dnevniksToAdd, ...dnevnikCollection];
    }
    return dnevnikCollection;
  }

  getDeca(id: string): any {
    //const options = createRequestOption(id);
    return this.http.get<DeteDTO[]>(this.resourceUrl + '/getDeca/' + id);
  }

  protected convertDateFromClient(dnevnik: IDnevnik): IDnevnik {
    return Object.assign({}, dnevnik, {
      pocetakVazenja: dnevnik.pocetakVazenja?.isValid() ? dnevnik.pocetakVazenja.format(DATE_FORMAT) : undefined,
      krajVazenja: dnevnik.krajVazenja?.isValid() ? dnevnik.krajVazenja.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.pocetakVazenja = res.body.pocetakVazenja ? dayjs(res.body.pocetakVazenja) : undefined;
      res.body.krajVazenja = res.body.krajVazenja ? dayjs(res.body.krajVazenja) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((dnevnik: IDnevnik) => {
        dnevnik.pocetakVazenja = dnevnik.pocetakVazenja ? dayjs(dnevnik.pocetakVazenja) : undefined;
        dnevnik.krajVazenja = dnevnik.krajVazenja ? dayjs(dnevnik.krajVazenja) : undefined;
      });
    }
    return res;
  }
}
