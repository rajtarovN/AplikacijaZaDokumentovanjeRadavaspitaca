import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVaspitac, getVaspitacIdentifier, VaspitacDTO, VaspitacZaGrupuDTO } from '../vaspitac.model';
import { ObjekatDTO } from '../../objekat/objekat.model';

export type EntityResponseType = HttpResponse<IVaspitac>;
export type EntityArrayResponseType = HttpResponse<IVaspitac[]>;

@Injectable({ providedIn: 'root' })
export class VaspitacService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vaspitacs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vaspitac: IVaspitac): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaspitac);
    return this.http
      .post<IVaspitac>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vaspitac: IVaspitac): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaspitac);
    return this.http
      .put<IVaspitac>(`${this.resourceUrl}/${getVaspitacIdentifier(vaspitac) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vaspitac: IVaspitac): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaspitac);
    return this.http
      .patch<IVaspitac>(`${this.resourceUrl}/${getVaspitacIdentifier(vaspitac) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVaspitac>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVaspitac[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVaspitacToCollectionIfMissing(vaspitacCollection: IVaspitac[], ...vaspitacsToCheck: (IVaspitac | null | undefined)[]): IVaspitac[] {
    const vaspitacs: IVaspitac[] = vaspitacsToCheck.filter(isPresent);
    if (vaspitacs.length > 0) {
      const vaspitacCollectionIdentifiers = vaspitacCollection.map(vaspitacItem => getVaspitacIdentifier(vaspitacItem)!);
      const vaspitacsToAdd = vaspitacs.filter(vaspitacItem => {
        const vaspitacIdentifier = getVaspitacIdentifier(vaspitacItem);
        if (vaspitacIdentifier == null || vaspitacCollectionIdentifiers.includes(vaspitacIdentifier)) {
          return false;
        }
        vaspitacCollectionIdentifiers.push(vaspitacIdentifier);
        return true;
      });
      return [...vaspitacsToAdd, ...vaspitacCollection];
    }
    return vaspitacCollection;
  }

  getByObjekat(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<VaspitacDTO[]>(this.resourceUrl + '/getByObjekat/1', { params: options, observe: 'response' });
  }

  getImena(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<VaspitacZaGrupuDTO[]>(this.resourceUrl + '/getImena', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(vaspitac: IVaspitac): IVaspitac {
    return Object.assign({}, vaspitac, {
      datumZaposlenja: vaspitac.datumZaposlenja?.isValid() ? vaspitac.datumZaposlenja.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.datumZaposlenja = res.body.datumZaposlenja ? dayjs(res.body.datumZaposlenja) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vaspitac: IVaspitac) => {
        vaspitac.datumZaposlenja = vaspitac.datumZaposlenja ? dayjs(vaspitac.datumZaposlenja) : undefined;
      });
    }
    return res;
  }
}
