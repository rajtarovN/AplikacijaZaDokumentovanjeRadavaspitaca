import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { INeDolasci, NeDolasci } from '../ne-dolasci.model';

import { NeDolasciService } from './ne-dolasci.service';

describe('NeDolasci Service', () => {
  let service: NeDolasciService;
  let httpMock: HttpTestingController;
  let elemDefault: INeDolasci;
  let expectedResult: INeDolasci | INeDolasci[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NeDolasciService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      datum: currentDate,
      razlog: 'AAAAAAA',
      id: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          datum: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a NeDolasci', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          datum: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datum: currentDate,
        },
        returnedFromService
      );

      service.create(new NeDolasci()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NeDolasci', () => {
      const returnedFromService = Object.assign(
        {
          datum: currentDate.format(DATE_FORMAT),
          razlog: 'BBBBBB',
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datum: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NeDolasci', () => {
      const patchObject = Object.assign(
        {
          datum: currentDate.format(DATE_FORMAT),
          razlog: 'BBBBBB',
        },
        new NeDolasci()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          datum: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NeDolasci', () => {
      const returnedFromService = Object.assign(
        {
          datum: currentDate.format(DATE_FORMAT),
          razlog: 'BBBBBB',
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datum: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a NeDolasci', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNeDolasciToCollectionIfMissing', () => {
      it('should add a NeDolasci to an empty array', () => {
        const neDolasci: INeDolasci = { id: 123 };
        expectedResult = service.addNeDolasciToCollectionIfMissing([], neDolasci);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(neDolasci);
      });

      it('should not add a NeDolasci to an array that contains it', () => {
        const neDolasci: INeDolasci = { id: 123 };
        const neDolasciCollection: INeDolasci[] = [
          {
            ...neDolasci,
          },
          { id: 456 },
        ];
        expectedResult = service.addNeDolasciToCollectionIfMissing(neDolasciCollection, neDolasci);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NeDolasci to an array that doesn't contain it", () => {
        const neDolasci: INeDolasci = { id: 123 };
        const neDolasciCollection: INeDolasci[] = [{ id: 456 }];
        expectedResult = service.addNeDolasciToCollectionIfMissing(neDolasciCollection, neDolasci);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(neDolasci);
      });

      it('should add only unique NeDolasci to an array', () => {
        const neDolasciArray: INeDolasci[] = [{ id: 123 }, { id: 456 }, { id: 21847 }];
        const neDolasciCollection: INeDolasci[] = [{ id: 123 }];
        expectedResult = service.addNeDolasciToCollectionIfMissing(neDolasciCollection, ...neDolasciArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const neDolasci: INeDolasci = { id: 123 };
        const neDolasci2: INeDolasci = { id: 456 };
        expectedResult = service.addNeDolasciToCollectionIfMissing([], neDolasci, neDolasci2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(neDolasci);
        expect(expectedResult).toContain(neDolasci2);
      });

      it('should accept null and undefined values', () => {
        const neDolasci: INeDolasci = { id: 123 };
        expectedResult = service.addNeDolasciToCollectionIfMissing([], null, neDolasci, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(neDolasci);
      });

      it('should return initial array if no NeDolasci is added', () => {
        const neDolasciCollection: INeDolasci[] = [{ id: 123 }];
        expectedResult = service.addNeDolasciToCollectionIfMissing(neDolasciCollection, undefined, null);
        expect(expectedResult).toEqual(neDolasciCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
