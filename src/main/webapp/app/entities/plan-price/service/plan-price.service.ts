import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlanPrice, getPlanPriceIdentifier } from '../plan-price.model';

export type EntityResponseType = HttpResponse<IPlanPrice>;
export type EntityArrayResponseType = HttpResponse<IPlanPrice[]>;

@Injectable({ providedIn: 'root' })
export class PlanPriceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/plan-prices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(planPrice: IPlanPrice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planPrice);
    return this.http
      .post<IPlanPrice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(planPrice: IPlanPrice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planPrice);
    return this.http
      .put<IPlanPrice>(`${this.resourceUrl}/${getPlanPriceIdentifier(planPrice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(planPrice: IPlanPrice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planPrice);
    return this.http
      .patch<IPlanPrice>(`${this.resourceUrl}/${getPlanPriceIdentifier(planPrice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPlanPrice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPlanPrice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlanPriceToCollectionIfMissing(
    planPriceCollection: IPlanPrice[],
    ...planPricesToCheck: (IPlanPrice | null | undefined)[]
  ): IPlanPrice[] {
    const planPrices: IPlanPrice[] = planPricesToCheck.filter(isPresent);
    if (planPrices.length > 0) {
      const planPriceCollectionIdentifiers = planPriceCollection.map(planPriceItem => getPlanPriceIdentifier(planPriceItem)!);
      const planPricesToAdd = planPrices.filter(planPriceItem => {
        const planPriceIdentifier = getPlanPriceIdentifier(planPriceItem);
        if (planPriceIdentifier == null || planPriceCollectionIdentifiers.includes(planPriceIdentifier)) {
          return false;
        }
        planPriceCollectionIdentifiers.push(planPriceIdentifier);
        return true;
      });
      return [...planPricesToAdd, ...planPriceCollection];
    }
    return planPriceCollection;
  }

  protected convertDateFromClient(planPrice: IPlanPrice): IPlanPrice {
    return Object.assign({}, planPrice, {
      datumPocetka: planPrice.datumPocetka?.isValid() ? planPrice.datumPocetka.format(DATE_FORMAT) : undefined,
      datumZavrsetka: planPrice.datumZavrsetka?.isValid() ? planPrice.datumZavrsetka.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.datumPocetka = res.body.datumPocetka ? dayjs(res.body.datumPocetka) : undefined;
      res.body.datumZavrsetka = res.body.datumZavrsetka ? dayjs(res.body.datumZavrsetka) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((planPrice: IPlanPrice) => {
        planPrice.datumPocetka = planPrice.datumPocetka ? dayjs(planPrice.datumPocetka) : undefined;
        planPrice.datumZavrsetka = planPrice.datumZavrsetka ? dayjs(planPrice.datumZavrsetka) : undefined;
      });
    }
    return res;
  }
}
