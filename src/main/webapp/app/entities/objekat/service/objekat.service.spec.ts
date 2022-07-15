import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IObjekat, Objekat } from '../objekat.model';

import { ObjekatService } from './objekat.service';

describe('Objekat Service', () => {
  let service: ObjekatService;
  let httpMock: HttpTestingController;
  let elemDefault: IObjekat;
  let expectedResult: IObjekat | IObjekat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ObjekatService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      opis: 'AAAAAAA',
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

    it('should create a Objekat', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Objekat()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Objekat', () => {
      const returnedFromService = Object.assign(
        {
          opis: 'BBBBBB',
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

    it('should partial update a Objekat', () => {
      const patchObject = Object.assign(
        {
          opis: 'BBBBBB',
        },
        new Objekat()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Objekat', () => {
      const returnedFromService = Object.assign(
        {
          opis: 'BBBBBB',
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

    it('should delete a Objekat', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addObjekatToCollectionIfMissing', () => {
      it('should add a Objekat to an empty array', () => {
        const objekat: IObjekat = { id: 123 };
        expectedResult = service.addObjekatToCollectionIfMissing([], objekat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(objekat);
      });

      it('should not add a Objekat to an array that contains it', () => {
        const objekat: IObjekat = { id: 123 };
        const objekatCollection: IObjekat[] = [
          {
            ...objekat,
          },
          { id: 456 },
        ];
        expectedResult = service.addObjekatToCollectionIfMissing(objekatCollection, objekat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Objekat to an array that doesn't contain it", () => {
        const objekat: IObjekat = { id: 123 };
        const objekatCollection: IObjekat[] = [{ id: 456 }];
        expectedResult = service.addObjekatToCollectionIfMissing(objekatCollection, objekat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(objekat);
      });

      it('should add only unique Objekat to an array', () => {
        const objekatArray: IObjekat[] = [{ id: 123 }, { id: 456 }, { id: 78278 }];
        const objekatCollection: IObjekat[] = [{ id: 123 }];
        expectedResult = service.addObjekatToCollectionIfMissing(objekatCollection, ...objekatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const objekat: IObjekat = { id: 123 };
        const objekat2: IObjekat = { id: 456 };
        expectedResult = service.addObjekatToCollectionIfMissing([], objekat, objekat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(objekat);
        expect(expectedResult).toContain(objekat2);
      });

      it('should accept null and undefined values', () => {
        const objekat: IObjekat = { id: 123 };
        expectedResult = service.addObjekatToCollectionIfMissing([], null, objekat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(objekat);
      });

      it('should return initial array if no Objekat is added', () => {
        const objekatCollection: IObjekat[] = [{ id: 123 }];
        expectedResult = service.addObjekatToCollectionIfMissing(objekatCollection, undefined, null);
        expect(expectedResult).toEqual(objekatCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
