import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IVaspitac, Vaspitac } from '../vaspitac.model';

import { VaspitacService } from './vaspitac.service';

describe('Vaspitac Service', () => {
  let service: VaspitacService;
  let httpMock: HttpTestingController;
  let elemDefault: IVaspitac;
  let expectedResult: IVaspitac | IVaspitac[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VaspitacService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      datumZaposlenja: currentDate,
      godineIskustva: 0,
      opis: 'AAAAAAA',
      id: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          datumZaposlenja: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Vaspitac', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          datumZaposlenja: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datumZaposlenja: currentDate,
        },
        returnedFromService
      );

      service.create(new Vaspitac()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vaspitac', () => {
      const returnedFromService = Object.assign(
        {
          datumZaposlenja: currentDate.format(DATE_FORMAT),
          godineIskustva: 1,
          opis: 'BBBBBB',
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datumZaposlenja: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vaspitac', () => {
      const patchObject = Object.assign(
        {
          datumZaposlenja: currentDate.format(DATE_FORMAT),
          opis: 'BBBBBB',
        },
        new Vaspitac()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          datumZaposlenja: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vaspitac', () => {
      const returnedFromService = Object.assign(
        {
          datumZaposlenja: currentDate.format(DATE_FORMAT),
          godineIskustva: 1,
          opis: 'BBBBBB',
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datumZaposlenja: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Vaspitac', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVaspitacToCollectionIfMissing', () => {
      it('should add a Vaspitac to an empty array', () => {
        const vaspitac: IVaspitac = { id: 123 };
        expectedResult = service.addVaspitacToCollectionIfMissing([], vaspitac);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vaspitac);
      });

      it('should not add a Vaspitac to an array that contains it', () => {
        const vaspitac: IVaspitac = { id: 123 };
        const vaspitacCollection: IVaspitac[] = [
          {
            ...vaspitac,
          },
          { id: 456 },
        ];
        expectedResult = service.addVaspitacToCollectionIfMissing(vaspitacCollection, vaspitac);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vaspitac to an array that doesn't contain it", () => {
        const vaspitac: IVaspitac = { id: 123 };
        const vaspitacCollection: IVaspitac[] = [{ id: 456 }];
        expectedResult = service.addVaspitacToCollectionIfMissing(vaspitacCollection, vaspitac);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vaspitac);
      });

      it('should add only unique Vaspitac to an array', () => {
        const vaspitacArray: IVaspitac[] = [{ id: 123 }, { id: 456 }, { id: 24611 }];
        const vaspitacCollection: IVaspitac[] = [{ id: 123 }];
        expectedResult = service.addVaspitacToCollectionIfMissing(vaspitacCollection, ...vaspitacArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vaspitac: IVaspitac = { id: 123 };
        const vaspitac2: IVaspitac = { id: 456 };
        expectedResult = service.addVaspitacToCollectionIfMissing([], vaspitac, vaspitac2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vaspitac);
        expect(expectedResult).toContain(vaspitac2);
      });

      it('should accept null and undefined values', () => {
        const vaspitac: IVaspitac = { id: 123 };
        expectedResult = service.addVaspitacToCollectionIfMissing([], null, vaspitac, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vaspitac);
      });

      it('should return initial array if no Vaspitac is added', () => {
        const vaspitacCollection: IVaspitac[] = [{ id: 123 }];
        expectedResult = service.addVaspitacToCollectionIfMissing(vaspitacCollection, undefined, null);
        expect(expectedResult).toEqual(vaspitacCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
