import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IKonacnaPrica, KonacnaPrica } from '../konacna-prica.model';

import { KonacnaPricaService } from './konacna-prica.service';

describe('KonacnaPrica Service', () => {
  let service: KonacnaPricaService;
  let httpMock: HttpTestingController;
  let elemDefault: IKonacnaPrica;
  let expectedResult: IKonacnaPrica | IKonacnaPrica[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(KonacnaPricaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      tekst: 'AAAAAAA',
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

    it('should create a KonacnaPrica', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new KonacnaPrica()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a KonacnaPrica', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tekst: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a KonacnaPrica', () => {
      const patchObject = Object.assign(
        {
          tekst: 'BBBBBB',
        },
        new KonacnaPrica()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of KonacnaPrica', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tekst: 'BBBBBB',
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

    it('should delete a KonacnaPrica', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addKonacnaPricaToCollectionIfMissing', () => {
      it('should add a KonacnaPrica to an empty array', () => {
        const konacnaPrica: IKonacnaPrica = { id: 123 };
        expectedResult = service.addKonacnaPricaToCollectionIfMissing([], konacnaPrica);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(konacnaPrica);
      });

      it('should not add a KonacnaPrica to an array that contains it', () => {
        const konacnaPrica: IKonacnaPrica = { id: 123 };
        const konacnaPricaCollection: IKonacnaPrica[] = [
          {
            ...konacnaPrica,
          },
          { id: 456 },
        ];
        expectedResult = service.addKonacnaPricaToCollectionIfMissing(konacnaPricaCollection, konacnaPrica);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a KonacnaPrica to an array that doesn't contain it", () => {
        const konacnaPrica: IKonacnaPrica = { id: 123 };
        const konacnaPricaCollection: IKonacnaPrica[] = [{ id: 456 }];
        expectedResult = service.addKonacnaPricaToCollectionIfMissing(konacnaPricaCollection, konacnaPrica);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(konacnaPrica);
      });

      it('should add only unique KonacnaPrica to an array', () => {
        const konacnaPricaArray: IKonacnaPrica[] = [{ id: 123 }, { id: 456 }, { id: 66915 }];
        const konacnaPricaCollection: IKonacnaPrica[] = [{ id: 123 }];
        expectedResult = service.addKonacnaPricaToCollectionIfMissing(konacnaPricaCollection, ...konacnaPricaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const konacnaPrica: IKonacnaPrica = { id: 123 };
        const konacnaPrica2: IKonacnaPrica = { id: 456 };
        expectedResult = service.addKonacnaPricaToCollectionIfMissing([], konacnaPrica, konacnaPrica2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(konacnaPrica);
        expect(expectedResult).toContain(konacnaPrica2);
      });

      it('should accept null and undefined values', () => {
        const konacnaPrica: IKonacnaPrica = { id: 123 };
        expectedResult = service.addKonacnaPricaToCollectionIfMissing([], null, konacnaPrica, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(konacnaPrica);
      });

      it('should return initial array if no KonacnaPrica is added', () => {
        const konacnaPricaCollection: IKonacnaPrica[] = [{ id: 123 }];
        expectedResult = service.addKonacnaPricaToCollectionIfMissing(konacnaPricaCollection, undefined, null);
        expect(expectedResult).toEqual(konacnaPricaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
