import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IKomentarNaPricu, KomentarNaPricu } from '../komentar-na-pricu.model';

import { KomentarNaPricuService } from './komentar-na-pricu.service';

describe('KomentarNaPricu Service', () => {
  let service: KomentarNaPricuService;
  let httpMock: HttpTestingController;
  let elemDefault: IKomentarNaPricu;
  let expectedResult: IKomentarNaPricu | IKomentarNaPricu[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(KomentarNaPricuService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      tekst: 'AAAAAAA',
      datum: currentDate,
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

    it('should create a KomentarNaPricu', () => {
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

      service.create(new KomentarNaPricu()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a KomentarNaPricu', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tekst: 'BBBBBB',
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

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a KomentarNaPricu', () => {
      const patchObject = Object.assign({}, new KomentarNaPricu());

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

    it('should return a list of KomentarNaPricu', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tekst: 'BBBBBB',
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

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a KomentarNaPricu', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addKomentarNaPricuToCollectionIfMissing', () => {
      it('should add a KomentarNaPricu to an empty array', () => {
        const komentarNaPricu: IKomentarNaPricu = { id: 123 };
        expectedResult = service.addKomentarNaPricuToCollectionIfMissing([], komentarNaPricu);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(komentarNaPricu);
      });

      it('should not add a KomentarNaPricu to an array that contains it', () => {
        const komentarNaPricu: IKomentarNaPricu = { id: 123 };
        const komentarNaPricuCollection: IKomentarNaPricu[] = [
          {
            ...komentarNaPricu,
          },
          { id: 456 },
        ];
        expectedResult = service.addKomentarNaPricuToCollectionIfMissing(komentarNaPricuCollection, komentarNaPricu);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a KomentarNaPricu to an array that doesn't contain it", () => {
        const komentarNaPricu: IKomentarNaPricu = { id: 123 };
        const komentarNaPricuCollection: IKomentarNaPricu[] = [{ id: 456 }];
        expectedResult = service.addKomentarNaPricuToCollectionIfMissing(komentarNaPricuCollection, komentarNaPricu);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(komentarNaPricu);
      });

      it('should add only unique KomentarNaPricu to an array', () => {
        const komentarNaPricuArray: IKomentarNaPricu[] = [{ id: 123 }, { id: 456 }, { id: 24959 }];
        const komentarNaPricuCollection: IKomentarNaPricu[] = [{ id: 123 }];
        expectedResult = service.addKomentarNaPricuToCollectionIfMissing(komentarNaPricuCollection, ...komentarNaPricuArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const komentarNaPricu: IKomentarNaPricu = { id: 123 };
        const komentarNaPricu2: IKomentarNaPricu = { id: 456 };
        expectedResult = service.addKomentarNaPricuToCollectionIfMissing([], komentarNaPricu, komentarNaPricu2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(komentarNaPricu);
        expect(expectedResult).toContain(komentarNaPricu2);
      });

      it('should accept null and undefined values', () => {
        const komentarNaPricu: IKomentarNaPricu = { id: 123 };
        expectedResult = service.addKomentarNaPricuToCollectionIfMissing([], null, komentarNaPricu, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(komentarNaPricu);
      });

      it('should return initial array if no KomentarNaPricu is added', () => {
        const komentarNaPricuCollection: IKomentarNaPricu[] = [{ id: 123 }];
        expectedResult = service.addKomentarNaPricuToCollectionIfMissing(komentarNaPricuCollection, undefined, null);
        expect(expectedResult).toEqual(komentarNaPricuCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
