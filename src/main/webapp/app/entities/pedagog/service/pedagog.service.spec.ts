import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPedagog, Pedagog } from '../pedagog.model';

import { PedagogService } from './pedagog.service';

describe('Pedagog Service', () => {
  let service: PedagogService;
  let httpMock: HttpTestingController;
  let elemDefault: IPedagog;
  let expectedResult: IPedagog | IPedagog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PedagogService);
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

    it('should create a Pedagog', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Pedagog()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pedagog', () => {
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

    it('should partial update a Pedagog', () => {
      const patchObject = Object.assign({}, new Pedagog());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pedagog', () => {
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

    it('should delete a Pedagog', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPedagogToCollectionIfMissing', () => {
      it('should add a Pedagog to an empty array', () => {
        const pedagog: IPedagog = { id: 123 };
        expectedResult = service.addPedagogToCollectionIfMissing([], pedagog);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pedagog);
      });

      it('should not add a Pedagog to an array that contains it', () => {
        const pedagog: IPedagog = { id: 123 };
        const pedagogCollection: IPedagog[] = [
          {
            ...pedagog,
          },
          { id: 456 },
        ];
        expectedResult = service.addPedagogToCollectionIfMissing(pedagogCollection, pedagog);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pedagog to an array that doesn't contain it", () => {
        const pedagog: IPedagog = { id: 123 };
        const pedagogCollection: IPedagog[] = [{ id: 456 }];
        expectedResult = service.addPedagogToCollectionIfMissing(pedagogCollection, pedagog);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pedagog);
      });

      it('should add only unique Pedagog to an array', () => {
        const pedagogArray: IPedagog[] = [{ id: 123 }, { id: 456 }, { id: 80540 }];
        const pedagogCollection: IPedagog[] = [{ id: 123 }];
        expectedResult = service.addPedagogToCollectionIfMissing(pedagogCollection, ...pedagogArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pedagog: IPedagog = { id: 123 };
        const pedagog2: IPedagog = { id: 456 };
        expectedResult = service.addPedagogToCollectionIfMissing([], pedagog, pedagog2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pedagog);
        expect(expectedResult).toContain(pedagog2);
      });

      it('should accept null and undefined values', () => {
        const pedagog: IPedagog = { id: 123 };
        expectedResult = service.addPedagogToCollectionIfMissing([], null, pedagog, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pedagog);
      });

      it('should return initial array if no Pedagog is added', () => {
        const pedagogCollection: IPedagog[] = [{ id: 123 }];
        expectedResult = service.addPedagogToCollectionIfMissing(pedagogCollection, undefined, null);
        expect(expectedResult).toEqual(pedagogCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
