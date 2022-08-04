import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INeDolasci, getNeDolasciIdentifier, NeDolasciDTO } from '../ne-dolasci.model';

export type EntityResponseType = HttpResponse<INeDolasci>;
export type EntityArrayResponseType = HttpResponse<INeDolasci[]>;

@Injectable({ providedIn: 'root' })
export class NeDolasciService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ne-dolascis');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(neDolasci: INeDolasci): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(neDolasci);
    return this.http
      .post<INeDolasci>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(neDolasci: INeDolasci): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(neDolasci);
    return this.http
      .put<INeDolasci>(`${this.resourceUrl}/${getNeDolasciIdentifier(neDolasci) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(neDolasci: INeDolasci): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(neDolasci);
    return this.http
      .patch<INeDolasci>(`${this.resourceUrl}/${getNeDolasciIdentifier(neDolasci) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INeDolasci>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INeDolasci[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNeDolasciToCollectionIfMissing(
    neDolasciCollection: INeDolasci[],
    ...neDolascisToCheck: (INeDolasci | null | undefined)[]
  ): INeDolasci[] {
    const neDolascis: INeDolasci[] = neDolascisToCheck.filter(isPresent);
    if (neDolascis.length > 0) {
      const neDolasciCollectionIdentifiers = neDolasciCollection.map(neDolasciItem => getNeDolasciIdentifier(neDolasciItem)!);
      const neDolascisToAdd = neDolascis.filter(neDolasciItem => {
        const neDolasciIdentifier = getNeDolasciIdentifier(neDolasciItem);
        if (neDolasciIdentifier == null || neDolasciCollectionIdentifiers.includes(neDolasciIdentifier)) {
          return false;
        }
        neDolasciCollectionIdentifiers.push(neDolasciIdentifier);
        return true;
      });
      return [...neDolascisToAdd, ...neDolasciCollection];
    }
    return neDolasciCollection;
  }

  queryByGrupa(req: { size: number; page: number; sort: string[] }, idGrupe: string): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INeDolasci[]>(this.resourceUrl + '/findByGrupa/' + String(idGrupe), { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  createNeDolasci(listaIzostanaka: NeDolasciDTO[]): Observable<EntityResponseType> {
    //const copy = this.convertDateFromClient(listaIzostanaka);
    return this.http
      .post<INeDolasci>(this.resourceUrl + '/create-list', listaIzostanaka, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  protected convertDateFromClient(neDolasci: INeDolasci): INeDolasci {
    return Object.assign({}, neDolasci, {
      datum: neDolasci.datum?.isValid() ? neDolasci.datum.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.datum = res.body.datum ? dayjs(res.body.datum) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((neDolasci: INeDolasci) => {
        neDolasci.datum = neDolasci.datum ? dayjs(neDolasci.datum) : undefined;
      });
    }
    return res;
  }
}
