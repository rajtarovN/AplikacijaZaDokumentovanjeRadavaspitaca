import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IZapazanjeUVeziDeteta, getZapazanjeUVeziDetetaIdentifier } from '../zapazanje-u-vezi-deteta.model';

export type EntityResponseType = HttpResponse<IZapazanjeUVeziDeteta>;
export type EntityArrayResponseType = HttpResponse<IZapazanjeUVeziDeteta[]>;

@Injectable({ providedIn: 'root' })
export class ZapazanjeUVeziDetetaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/zapazanje-u-vezi-detetas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(zapazanjeUVeziDeteta);
    return this.http
      .post<IZapazanjeUVeziDeteta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(zapazanjeUVeziDeteta);
    return this.http
      .put<IZapazanjeUVeziDeteta>(`${this.resourceUrl}/${getZapazanjeUVeziDetetaIdentifier(zapazanjeUVeziDeteta) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(zapazanjeUVeziDeteta);
    return this.http
      .patch<IZapazanjeUVeziDeteta>(`${this.resourceUrl}/${getZapazanjeUVeziDetetaIdentifier(zapazanjeUVeziDeteta) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IZapazanjeUVeziDeteta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IZapazanjeUVeziDeteta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
  queryByDete(idDeteta: string): Observable<EntityArrayResponseType> {
    return this.http.get<IZapazanjeUVeziDeteta[]>(this.resourceUrl + '/findByDete/' + idDeteta, { observe: 'response' });
  }
  addZapazanjeUVeziDetetaToCollectionIfMissing(
    zapazanjeUVeziDetetaCollection: IZapazanjeUVeziDeteta[],
    ...zapazanjeUVeziDetetasToCheck: (IZapazanjeUVeziDeteta | null | undefined)[]
  ): IZapazanjeUVeziDeteta[] {
    const zapazanjeUVeziDetetas: IZapazanjeUVeziDeteta[] = zapazanjeUVeziDetetasToCheck.filter(isPresent);
    if (zapazanjeUVeziDetetas.length > 0) {
      const zapazanjeUVeziDetetaCollectionIdentifiers = zapazanjeUVeziDetetaCollection.map(
        zapazanjeUVeziDetetaItem => getZapazanjeUVeziDetetaIdentifier(zapazanjeUVeziDetetaItem)!
      );
      const zapazanjeUVeziDetetasToAdd = zapazanjeUVeziDetetas.filter(zapazanjeUVeziDetetaItem => {
        const zapazanjeUVeziDetetaIdentifier = getZapazanjeUVeziDetetaIdentifier(zapazanjeUVeziDetetaItem);
        if (zapazanjeUVeziDetetaIdentifier == null || zapazanjeUVeziDetetaCollectionIdentifiers.includes(zapazanjeUVeziDetetaIdentifier)) {
          return false;
        }
        zapazanjeUVeziDetetaCollectionIdentifiers.push(zapazanjeUVeziDetetaIdentifier);
        return true;
      });
      return [...zapazanjeUVeziDetetasToAdd, ...zapazanjeUVeziDetetaCollection];
    }
    return zapazanjeUVeziDetetaCollection;
  }

  protected convertDateFromClient(zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta): IZapazanjeUVeziDeteta {
    return Object.assign({}, zapazanjeUVeziDeteta, {
      datum: zapazanjeUVeziDeteta.datum?.isValid() ? zapazanjeUVeziDeteta.datum.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta) => {
        zapazanjeUVeziDeteta.datum = zapazanjeUVeziDeteta.datum ? dayjs(zapazanjeUVeziDeteta.datum) : undefined;
      });
    }
    return res;
  }
}
