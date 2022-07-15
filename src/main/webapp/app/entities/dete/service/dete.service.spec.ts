import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDete, Dete } from '../dete.model';

import { DeteService } from './dete.service';

describe('Dete Service', () => {
  let service: DeteService;
  let httpMock: HttpTestingController;
  let elemDefault: IDete;
  let expectedResult: IDete | IDete[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      visina: 0,
      tezina: 0,
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

    it('should create a Dete', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Dete()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dete', () => {
      const returnedFromService = Object.assign(
        {
          visina: 1,
          tezina: 1,
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

    it('should partial update a Dete', () => {
      const patchObject = Object.assign(
        {
          tezina: 1,
        },
        new Dete()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dete', () => {
      const returnedFromService = Object.assign(
        {
          visina: 1,
          tezina: 1,
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

    it('should delete a Dete', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDeteToCollectionIfMissing', () => {
      it('should add a Dete to an empty array', () => {
        const dete: IDete = { id: 123 };
        expectedResult = service.addDeteToCollectionIfMissing([], dete);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dete);
      });

      it('should not add a Dete to an array that contains it', () => {
        const dete: IDete = { id: 123 };
        const deteCollection: IDete[] = [
          {
            ...dete,
          },
          { id: 456 },
        ];
        expectedResult = service.addDeteToCollectionIfMissing(deteCollection, dete);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dete to an array that doesn't contain it", () => {
        const dete: IDete = { id: 123 };
        const deteCollection: IDete[] = [{ id: 456 }];
        expectedResult = service.addDeteToCollectionIfMissing(deteCollection, dete);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dete);
      });

      it('should add only unique Dete to an array', () => {
        const deteArray: IDete[] = [{ id: 123 }, { id: 456 }, { id: 79206 }];
        const deteCollection: IDete[] = [{ id: 123 }];
        expectedResult = service.addDeteToCollectionIfMissing(deteCollection, ...deteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dete: IDete = { id: 123 };
        const dete2: IDete = { id: 456 };
        expectedResult = service.addDeteToCollectionIfMissing([], dete, dete2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dete);
        expect(expectedResult).toContain(dete2);
      });

      it('should accept null and undefined values', () => {
        const dete: IDete = { id: 123 };
        expectedResult = service.addDeteToCollectionIfMissing([], null, dete, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dete);
      });

      it('should return initial array if no Dete is added', () => {
        const deteCollection: IDete[] = [{ id: 123 }];
        expectedResult = service.addDeteToCollectionIfMissing(deteCollection, undefined, null);
        expect(expectedResult).toEqual(deteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
