import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRoditelj, Roditelj } from '../roditelj.model';

import { RoditeljService } from './roditelj.service';

describe('Roditelj Service', () => {
  let service: RoditeljService;
  let httpMock: HttpTestingController;
  let elemDefault: IRoditelj;
  let expectedResult: IRoditelj | IRoditelj[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RoditeljService);
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

    it('should create a Roditelj', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Roditelj()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Roditelj', () => {
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

    it('should partial update a Roditelj', () => {
      const patchObject = Object.assign({}, new Roditelj());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Roditelj', () => {
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

    it('should delete a Roditelj', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRoditeljToCollectionIfMissing', () => {
      it('should add a Roditelj to an empty array', () => {
        const roditelj: IRoditelj = { id: 123 };
        expectedResult = service.addRoditeljToCollectionIfMissing([], roditelj);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roditelj);
      });

      it('should not add a Roditelj to an array that contains it', () => {
        const roditelj: IRoditelj = { id: 123 };
        const roditeljCollection: IRoditelj[] = [
          {
            ...roditelj,
          },
          { id: 456 },
        ];
        expectedResult = service.addRoditeljToCollectionIfMissing(roditeljCollection, roditelj);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Roditelj to an array that doesn't contain it", () => {
        const roditelj: IRoditelj = { id: 123 };
        const roditeljCollection: IRoditelj[] = [{ id: 456 }];
        expectedResult = service.addRoditeljToCollectionIfMissing(roditeljCollection, roditelj);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roditelj);
      });

      it('should add only unique Roditelj to an array', () => {
        const roditeljArray: IRoditelj[] = [{ id: 123 }, { id: 456 }, { id: 36332 }];
        const roditeljCollection: IRoditelj[] = [{ id: 123 }];
        expectedResult = service.addRoditeljToCollectionIfMissing(roditeljCollection, ...roditeljArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const roditelj: IRoditelj = { id: 123 };
        const roditelj2: IRoditelj = { id: 456 };
        expectedResult = service.addRoditeljToCollectionIfMissing([], roditelj, roditelj2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roditelj);
        expect(expectedResult).toContain(roditelj2);
      });

      it('should accept null and undefined values', () => {
        const roditelj: IRoditelj = { id: 123 };
        expectedResult = service.addRoditeljToCollectionIfMissing([], null, roditelj, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roditelj);
      });

      it('should return initial array if no Roditelj is added', () => {
        const roditeljCollection: IRoditelj[] = [{ id: 123 }];
        expectedResult = service.addRoditeljToCollectionIfMissing(roditeljCollection, undefined, null);
        expect(expectedResult).toEqual(roditeljCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
