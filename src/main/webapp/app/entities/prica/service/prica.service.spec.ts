import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPrica, Prica } from '../prica.model';

import { PricaService } from './prica.service';

describe('Prica Service', () => {
  let service: PricaService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrica;
  let expectedResult: IPrica | IPrica[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PricaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Prica', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Prica()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Prica', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Prica', () => {
      const patchObject = Object.assign({}, new Prica());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Prica', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Prica', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPricaToCollectionIfMissing', () => {
      it('should add a Prica to an empty array', () => {
        const prica: IPrica = { id: 123 };
        expectedResult = service.addPricaToCollectionIfMissing([], prica);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prica);
      });

      it('should not add a Prica to an array that contains it', () => {
        const prica: IPrica = { id: 123 };
        const pricaCollection: IPrica[] = [
          {
            ...prica,
          },
          { id: 456 },
        ];
        expectedResult = service.addPricaToCollectionIfMissing(pricaCollection, prica);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Prica to an array that doesn't contain it", () => {
        const prica: IPrica = { id: 123 };
        const pricaCollection: IPrica[] = [{ id: 456 }];
        expectedResult = service.addPricaToCollectionIfMissing(pricaCollection, prica);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prica);
      });

      it('should add only unique Prica to an array', () => {
        const pricaArray: IPrica[] = [{ id: 123 }, { id: 456 }, { id: 9131 }];
        const pricaCollection: IPrica[] = [{ id: 123 }];
        expectedResult = service.addPricaToCollectionIfMissing(pricaCollection, ...pricaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prica: IPrica = { id: 123 };
        const prica2: IPrica = { id: 456 };
        expectedResult = service.addPricaToCollectionIfMissing([], prica, prica2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prica);
        expect(expectedResult).toContain(prica2);
      });

      it('should accept null and undefined values', () => {
        const prica: IPrica = { id: 123 };
        expectedResult = service.addPricaToCollectionIfMissing([], null, prica, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prica);
      });

      it('should return initial array if no Prica is added', () => {
        const pricaCollection: IPrica[] = [{ id: 123 }];
        expectedResult = service.addPricaToCollectionIfMissing(pricaCollection, undefined, null);
        expect(expectedResult).toEqual(pricaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
