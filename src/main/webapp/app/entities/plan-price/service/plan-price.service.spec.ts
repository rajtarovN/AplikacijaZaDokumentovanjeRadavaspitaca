import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPlanPrice, PlanPrice } from '../plan-price.model';

import { PlanPriceService } from './plan-price.service';

describe('PlanPrice Service', () => {
  let service: PlanPriceService;
  let httpMock: HttpTestingController;
  let elemDefault: IPlanPrice;
  let expectedResult: IPlanPrice | IPlanPrice[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlanPriceService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      izvoriSaznanja: 'AAAAAAA',
      nazivTeme: 'AAAAAAA',
      pocetnaIdeja: 'AAAAAAA',
      datumPocetka: currentDate,
      nacinUcescaVaspitaca: 'AAAAAAA',
      materijali: 'AAAAAAA',
      ucescePorodice: 'AAAAAAA',
      mesta: 'AAAAAAA',
      datumZavrsetka: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          datumPocetka: currentDate.format(DATE_FORMAT),
          datumZavrsetka: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PlanPrice', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          datumPocetka: currentDate.format(DATE_FORMAT),
          datumZavrsetka: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datumPocetka: currentDate,
          datumZavrsetka: currentDate,
        },
        returnedFromService
      );

      service.create(new PlanPrice()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlanPrice', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          izvoriSaznanja: 'BBBBBB',
          nazivTeme: 'BBBBBB',
          pocetnaIdeja: 'BBBBBB',
          datumPocetka: currentDate.format(DATE_FORMAT),
          nacinUcescaVaspitaca: 'BBBBBB',
          materijali: 'BBBBBB',
          ucescePorodice: 'BBBBBB',
          mesta: 'BBBBBB',
          datumZavrsetka: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datumPocetka: currentDate,
          datumZavrsetka: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlanPrice', () => {
      const patchObject = Object.assign(
        {
          izvoriSaznanja: 'BBBBBB',
          nazivTeme: 'BBBBBB',
          datumPocetka: currentDate.format(DATE_FORMAT),
          materijali: 'BBBBBB',
        },
        new PlanPrice()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          datumPocetka: currentDate,
          datumZavrsetka: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlanPrice', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          izvoriSaznanja: 'BBBBBB',
          nazivTeme: 'BBBBBB',
          pocetnaIdeja: 'BBBBBB',
          datumPocetka: currentDate.format(DATE_FORMAT),
          nacinUcescaVaspitaca: 'BBBBBB',
          materijali: 'BBBBBB',
          ucescePorodice: 'BBBBBB',
          mesta: 'BBBBBB',
          datumZavrsetka: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datumPocetka: currentDate,
          datumZavrsetka: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PlanPrice', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPlanPriceToCollectionIfMissing', () => {
      it('should add a PlanPrice to an empty array', () => {
        const planPrice: IPlanPrice = { id: 123 };
        expectedResult = service.addPlanPriceToCollectionIfMissing([], planPrice);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(planPrice);
      });

      it('should not add a PlanPrice to an array that contains it', () => {
        const planPrice: IPlanPrice = { id: 123 };
        const planPriceCollection: IPlanPrice[] = [
          {
            ...planPrice,
          },
          { id: 456 },
        ];
        expectedResult = service.addPlanPriceToCollectionIfMissing(planPriceCollection, planPrice);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlanPrice to an array that doesn't contain it", () => {
        const planPrice: IPlanPrice = { id: 123 };
        const planPriceCollection: IPlanPrice[] = [{ id: 456 }];
        expectedResult = service.addPlanPriceToCollectionIfMissing(planPriceCollection, planPrice);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(planPrice);
      });

      it('should add only unique PlanPrice to an array', () => {
        const planPriceArray: IPlanPrice[] = [{ id: 123 }, { id: 456 }, { id: 79912 }];
        const planPriceCollection: IPlanPrice[] = [{ id: 123 }];
        expectedResult = service.addPlanPriceToCollectionIfMissing(planPriceCollection, ...planPriceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const planPrice: IPlanPrice = { id: 123 };
        const planPrice2: IPlanPrice = { id: 456 };
        expectedResult = service.addPlanPriceToCollectionIfMissing([], planPrice, planPrice2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(planPrice);
        expect(expectedResult).toContain(planPrice2);
      });

      it('should accept null and undefined values', () => {
        const planPrice: IPlanPrice = { id: 123 };
        expectedResult = service.addPlanPriceToCollectionIfMissing([], null, planPrice, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(planPrice);
      });

      it('should return initial array if no PlanPrice is added', () => {
        const planPriceCollection: IPlanPrice[] = [{ id: 123 }];
        expectedResult = service.addPlanPriceToCollectionIfMissing(planPriceCollection, undefined, null);
        expect(expectedResult).toEqual(planPriceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
