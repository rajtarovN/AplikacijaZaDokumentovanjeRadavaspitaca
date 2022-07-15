import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDnevnik, Dnevnik } from '../dnevnik.model';

import { DnevnikService } from './dnevnik.service';

describe('Dnevnik Service', () => {
  let service: DnevnikService;
  let httpMock: HttpTestingController;
  let elemDefault: IDnevnik;
  let expectedResult: IDnevnik | IDnevnik[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DnevnikService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      pocetakVazenja: currentDate,
      krajVazenja: currentDate,
      id: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          pocetakVazenja: currentDate.format(DATE_FORMAT),
          krajVazenja: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Dnevnik', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          pocetakVazenja: currentDate.format(DATE_FORMAT),
          krajVazenja: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          pocetakVazenja: currentDate,
          krajVazenja: currentDate,
        },
        returnedFromService
      );

      service.create(new Dnevnik()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dnevnik', () => {
      const returnedFromService = Object.assign(
        {
          pocetakVazenja: currentDate.format(DATE_FORMAT),
          krajVazenja: currentDate.format(DATE_FORMAT),
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          pocetakVazenja: currentDate,
          krajVazenja: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Dnevnik', () => {
      const patchObject = Object.assign(
        {
          krajVazenja: currentDate.format(DATE_FORMAT),
        },
        new Dnevnik()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          pocetakVazenja: currentDate,
          krajVazenja: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dnevnik', () => {
      const returnedFromService = Object.assign(
        {
          pocetakVazenja: currentDate.format(DATE_FORMAT),
          krajVazenja: currentDate.format(DATE_FORMAT),
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          pocetakVazenja: currentDate,
          krajVazenja: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Dnevnik', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDnevnikToCollectionIfMissing', () => {
      it('should add a Dnevnik to an empty array', () => {
        const dnevnik: IDnevnik = { id: 123 };
        expectedResult = service.addDnevnikToCollectionIfMissing([], dnevnik);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dnevnik);
      });

      it('should not add a Dnevnik to an array that contains it', () => {
        const dnevnik: IDnevnik = { id: 123 };
        const dnevnikCollection: IDnevnik[] = [
          {
            ...dnevnik,
          },
          { id: 456 },
        ];
        expectedResult = service.addDnevnikToCollectionIfMissing(dnevnikCollection, dnevnik);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dnevnik to an array that doesn't contain it", () => {
        const dnevnik: IDnevnik = { id: 123 };
        const dnevnikCollection: IDnevnik[] = [{ id: 456 }];
        expectedResult = service.addDnevnikToCollectionIfMissing(dnevnikCollection, dnevnik);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dnevnik);
      });

      it('should add only unique Dnevnik to an array', () => {
        const dnevnikArray: IDnevnik[] = [{ id: 123 }, { id: 456 }, { id: 27187 }];
        const dnevnikCollection: IDnevnik[] = [{ id: 123 }];
        expectedResult = service.addDnevnikToCollectionIfMissing(dnevnikCollection, ...dnevnikArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dnevnik: IDnevnik = { id: 123 };
        const dnevnik2: IDnevnik = { id: 456 };
        expectedResult = service.addDnevnikToCollectionIfMissing([], dnevnik, dnevnik2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dnevnik);
        expect(expectedResult).toContain(dnevnik2);
      });

      it('should accept null and undefined values', () => {
        const dnevnik: IDnevnik = { id: 123 };
        expectedResult = service.addDnevnikToCollectionIfMissing([], null, dnevnik, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dnevnik);
      });

      it('should return initial array if no Dnevnik is added', () => {
        const dnevnikCollection: IDnevnik[] = [{ id: 123 }];
        expectedResult = service.addDnevnikToCollectionIfMissing(dnevnikCollection, undefined, null);
        expect(expectedResult).toEqual(dnevnikCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
