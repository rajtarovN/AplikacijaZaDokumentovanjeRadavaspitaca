import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { StatusMaterijala } from 'app/entities/enumerations/status-materijala.model';
import { IPotrebanMaterijal, PotrebanMaterijal } from '../potreban-materijal.model';

import { PotrebanMaterijalService } from './potreban-materijal.service';

describe('PotrebanMaterijal Service', () => {
  let service: PotrebanMaterijalService;
  let httpMock: HttpTestingController;
  let elemDefault: IPotrebanMaterijal;
  let expectedResult: IPotrebanMaterijal | IPotrebanMaterijal[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PotrebanMaterijalService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      naziv: 'AAAAAAA',
      kolicina: 0,
      id: 0,
      statusMaterijala: StatusMaterijala.NOV,
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

    it('should create a PotrebanMaterijal', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PotrebanMaterijal()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PotrebanMaterijal', () => {
      const returnedFromService = Object.assign(
        {
          naziv: 'BBBBBB',
          kolicina: 1,
          id: 1,
          statusMaterijala: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PotrebanMaterijal', () => {
      const patchObject = Object.assign(
        {
          naziv: 'BBBBBB',
          statusMaterijala: 'BBBBBB',
        },
        new PotrebanMaterijal()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PotrebanMaterijal', () => {
      const returnedFromService = Object.assign(
        {
          naziv: 'BBBBBB',
          kolicina: 1,
          id: 1,
          statusMaterijala: 'BBBBBB',
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

    it('should delete a PotrebanMaterijal', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPotrebanMaterijalToCollectionIfMissing', () => {
      it('should add a PotrebanMaterijal to an empty array', () => {
        const potrebanMaterijal: IPotrebanMaterijal = { id: 123 };
        expectedResult = service.addPotrebanMaterijalToCollectionIfMissing([], potrebanMaterijal);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(potrebanMaterijal);
      });

      it('should not add a PotrebanMaterijal to an array that contains it', () => {
        const potrebanMaterijal: IPotrebanMaterijal = { id: 123 };
        const potrebanMaterijalCollection: IPotrebanMaterijal[] = [
          {
            ...potrebanMaterijal,
          },
          { id: 456 },
        ];
        expectedResult = service.addPotrebanMaterijalToCollectionIfMissing(potrebanMaterijalCollection, potrebanMaterijal);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PotrebanMaterijal to an array that doesn't contain it", () => {
        const potrebanMaterijal: IPotrebanMaterijal = { id: 123 };
        const potrebanMaterijalCollection: IPotrebanMaterijal[] = [{ id: 456 }];
        expectedResult = service.addPotrebanMaterijalToCollectionIfMissing(potrebanMaterijalCollection, potrebanMaterijal);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(potrebanMaterijal);
      });

      it('should add only unique PotrebanMaterijal to an array', () => {
        const potrebanMaterijalArray: IPotrebanMaterijal[] = [{ id: 123 }, { id: 456 }, { id: 78737 }];
        const potrebanMaterijalCollection: IPotrebanMaterijal[] = [{ id: 123 }];
        expectedResult = service.addPotrebanMaterijalToCollectionIfMissing(potrebanMaterijalCollection, ...potrebanMaterijalArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const potrebanMaterijal: IPotrebanMaterijal = { id: 123 };
        const potrebanMaterijal2: IPotrebanMaterijal = { id: 456 };
        expectedResult = service.addPotrebanMaterijalToCollectionIfMissing([], potrebanMaterijal, potrebanMaterijal2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(potrebanMaterijal);
        expect(expectedResult).toContain(potrebanMaterijal2);
      });

      it('should accept null and undefined values', () => {
        const potrebanMaterijal: IPotrebanMaterijal = { id: 123 };
        expectedResult = service.addPotrebanMaterijalToCollectionIfMissing([], null, potrebanMaterijal, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(potrebanMaterijal);
      });

      it('should return initial array if no PotrebanMaterijal is added', () => {
        const potrebanMaterijalCollection: IPotrebanMaterijal[] = [{ id: 123 }];
        expectedResult = service.addPotrebanMaterijalToCollectionIfMissing(potrebanMaterijalCollection, undefined, null);
        expect(expectedResult).toEqual(potrebanMaterijalCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
