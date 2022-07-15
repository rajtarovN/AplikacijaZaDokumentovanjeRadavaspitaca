import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDirektor, Direktor } from '../direktor.model';

import { DirektorService } from './direktor.service';

describe('Direktor Service', () => {
  let service: DirektorService;
  let httpMock: HttpTestingController;
  let elemDefault: IDirektor;
  let expectedResult: IDirektor | IDirektor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DirektorService);
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

    it('should create a Direktor', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Direktor()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Direktor', () => {
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

    it('should partial update a Direktor', () => {
      const patchObject = Object.assign({}, new Direktor());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Direktor', () => {
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

    it('should delete a Direktor', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDirektorToCollectionIfMissing', () => {
      it('should add a Direktor to an empty array', () => {
        const direktor: IDirektor = { id: 123 };
        expectedResult = service.addDirektorToCollectionIfMissing([], direktor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(direktor);
      });

      it('should not add a Direktor to an array that contains it', () => {
        const direktor: IDirektor = { id: 123 };
        const direktorCollection: IDirektor[] = [
          {
            ...direktor,
          },
          { id: 456 },
        ];
        expectedResult = service.addDirektorToCollectionIfMissing(direktorCollection, direktor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Direktor to an array that doesn't contain it", () => {
        const direktor: IDirektor = { id: 123 };
        const direktorCollection: IDirektor[] = [{ id: 456 }];
        expectedResult = service.addDirektorToCollectionIfMissing(direktorCollection, direktor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(direktor);
      });

      it('should add only unique Direktor to an array', () => {
        const direktorArray: IDirektor[] = [{ id: 123 }, { id: 456 }, { id: 59701 }];
        const direktorCollection: IDirektor[] = [{ id: 123 }];
        expectedResult = service.addDirektorToCollectionIfMissing(direktorCollection, ...direktorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const direktor: IDirektor = { id: 123 };
        const direktor2: IDirektor = { id: 456 };
        expectedResult = service.addDirektorToCollectionIfMissing([], direktor, direktor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(direktor);
        expect(expectedResult).toContain(direktor2);
      });

      it('should accept null and undefined values', () => {
        const direktor: IDirektor = { id: 123 };
        expectedResult = service.addDirektorToCollectionIfMissing([], null, direktor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(direktor);
      });

      it('should return initial array if no Direktor is added', () => {
        const direktorCollection: IDirektor[] = [{ id: 123 }];
        expectedResult = service.addDirektorToCollectionIfMissing(direktorCollection, undefined, null);
        expect(expectedResult).toEqual(direktorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
