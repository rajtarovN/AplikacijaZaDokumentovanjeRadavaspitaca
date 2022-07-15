import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IKomentarNaPricu, getKomentarNaPricuIdentifier } from '../komentar-na-pricu.model';

export type EntityResponseType = HttpResponse<IKomentarNaPricu>;
export type EntityArrayResponseType = HttpResponse<IKomentarNaPricu[]>;

@Injectable({ providedIn: 'root' })
export class KomentarNaPricuService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/komentar-na-pricus');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(komentarNaPricu: IKomentarNaPricu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(komentarNaPricu);
    return this.http
      .post<IKomentarNaPricu>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(komentarNaPricu: IKomentarNaPricu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(komentarNaPricu);
    return this.http
      .put<IKomentarNaPricu>(`${this.resourceUrl}/${getKomentarNaPricuIdentifier(komentarNaPricu) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(komentarNaPricu: IKomentarNaPricu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(komentarNaPricu);
    return this.http
      .patch<IKomentarNaPricu>(`${this.resourceUrl}/${getKomentarNaPricuIdentifier(komentarNaPricu) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IKomentarNaPricu>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IKomentarNaPricu[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addKomentarNaPricuToCollectionIfMissing(
    komentarNaPricuCollection: IKomentarNaPricu[],
    ...komentarNaPricusToCheck: (IKomentarNaPricu | null | undefined)[]
  ): IKomentarNaPricu[] {
    const komentarNaPricus: IKomentarNaPricu[] = komentarNaPricusToCheck.filter(isPresent);
    if (komentarNaPricus.length > 0) {
      const komentarNaPricuCollectionIdentifiers = komentarNaPricuCollection.map(
        komentarNaPricuItem => getKomentarNaPricuIdentifier(komentarNaPricuItem)!
      );
      const komentarNaPricusToAdd = komentarNaPricus.filter(komentarNaPricuItem => {
        const komentarNaPricuIdentifier = getKomentarNaPricuIdentifier(komentarNaPricuItem);
        if (komentarNaPricuIdentifier == null || komentarNaPricuCollectionIdentifiers.includes(komentarNaPricuIdentifier)) {
          return false;
        }
        komentarNaPricuCollectionIdentifiers.push(komentarNaPricuIdentifier);
        return true;
      });
      return [...komentarNaPricusToAdd, ...komentarNaPricuCollection];
    }
    return komentarNaPricuCollection;
  }

  protected convertDateFromClient(komentarNaPricu: IKomentarNaPricu): IKomentarNaPricu {
    return Object.assign({}, komentarNaPricu, {
      datum: komentarNaPricu.datum?.isValid() ? komentarNaPricu.datum.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((komentarNaPricu: IKomentarNaPricu) => {
        komentarNaPricu.datum = komentarNaPricu.datum ? dayjs(komentarNaPricu.datum) : undefined;
      });
    }
    return res;
  }
}
