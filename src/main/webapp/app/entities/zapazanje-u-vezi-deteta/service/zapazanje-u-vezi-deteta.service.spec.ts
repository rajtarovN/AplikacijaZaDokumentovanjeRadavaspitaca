import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IZapazanjeUVeziDeteta, ZapazanjeUVeziDeteta } from '../zapazanje-u-vezi-deteta.model';

import { ZapazanjeUVeziDetetaService } from './zapazanje-u-vezi-deteta.service';

describe('ZapazanjeUVeziDeteta Service', () => {
  let service: ZapazanjeUVeziDetetaService;
  let httpMock: HttpTestingController;
  let elemDefault: IZapazanjeUVeziDeteta;
  let expectedResult: IZapazanjeUVeziDeteta | IZapazanjeUVeziDeteta[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ZapazanjeUVeziDetetaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      interesovanja: 'AAAAAAA',
      prednosti: 'AAAAAAA',
      mane: 'AAAAAAA',
      predlozi: 'AAAAAAA',
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

    it('should create a ZapazanjeUVeziDeteta', () => {
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

      service.create(new ZapazanjeUVeziDeteta()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ZapazanjeUVeziDeteta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          interesovanja: 'BBBBBB',
          prednosti: 'BBBBBB',
          mane: 'BBBBBB',
          predlozi: 'BBBBBB',
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

    it('should partial update a ZapazanjeUVeziDeteta', () => {
      const patchObject = Object.assign(
        {
          interesovanja: 'BBBBBB',
          prednosti: 'BBBBBB',
        },
        new ZapazanjeUVeziDeteta()
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

    it('should return a list of ZapazanjeUVeziDeteta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          interesovanja: 'BBBBBB',
          prednosti: 'BBBBBB',
          mane: 'BBBBBB',
          predlozi: 'BBBBBB',
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

    it('should delete a ZapazanjeUVeziDeteta', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addZapazanjeUVeziDetetaToCollectionIfMissing', () => {
      it('should add a ZapazanjeUVeziDeteta to an empty array', () => {
        const zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta = { id: 123 };
        expectedResult = service.addZapazanjeUVeziDetetaToCollectionIfMissing([], zapazanjeUVeziDeteta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(zapazanjeUVeziDeteta);
      });

      it('should not add a ZapazanjeUVeziDeteta to an array that contains it', () => {
        const zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta = { id: 123 };
        const zapazanjeUVeziDetetaCollection: IZapazanjeUVeziDeteta[] = [
          {
            ...zapazanjeUVeziDeteta,
          },
          { id: 456 },
        ];
        expectedResult = service.addZapazanjeUVeziDetetaToCollectionIfMissing(zapazanjeUVeziDetetaCollection, zapazanjeUVeziDeteta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ZapazanjeUVeziDeteta to an array that doesn't contain it", () => {
        const zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta = { id: 123 };
        const zapazanjeUVeziDetetaCollection: IZapazanjeUVeziDeteta[] = [{ id: 456 }];
        expectedResult = service.addZapazanjeUVeziDetetaToCollectionIfMissing(zapazanjeUVeziDetetaCollection, zapazanjeUVeziDeteta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(zapazanjeUVeziDeteta);
      });

      it('should add only unique ZapazanjeUVeziDeteta to an array', () => {
        const zapazanjeUVeziDetetaArray: IZapazanjeUVeziDeteta[] = [{ id: 123 }, { id: 456 }, { id: 31002 }];
        const zapazanjeUVeziDetetaCollection: IZapazanjeUVeziDeteta[] = [{ id: 123 }];
        expectedResult = service.addZapazanjeUVeziDetetaToCollectionIfMissing(zapazanjeUVeziDetetaCollection, ...zapazanjeUVeziDetetaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta = { id: 123 };
        const zapazanjeUVeziDeteta2: IZapazanjeUVeziDeteta = { id: 456 };
        expectedResult = service.addZapazanjeUVeziDetetaToCollectionIfMissing([], zapazanjeUVeziDeteta, zapazanjeUVeziDeteta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(zapazanjeUVeziDeteta);
        expect(expectedResult).toContain(zapazanjeUVeziDeteta2);
      });

      it('should accept null and undefined values', () => {
        const zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta = { id: 123 };
        expectedResult = service.addZapazanjeUVeziDetetaToCollectionIfMissing([], null, zapazanjeUVeziDeteta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(zapazanjeUVeziDeteta);
      });

      it('should return initial array if no ZapazanjeUVeziDeteta is added', () => {
        const zapazanjeUVeziDetetaCollection: IZapazanjeUVeziDeteta[] = [{ id: 123 }];
        expectedResult = service.addZapazanjeUVeziDetetaToCollectionIfMissing(zapazanjeUVeziDetetaCollection, undefined, null);
        expect(expectedResult).toEqual(zapazanjeUVeziDetetaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
