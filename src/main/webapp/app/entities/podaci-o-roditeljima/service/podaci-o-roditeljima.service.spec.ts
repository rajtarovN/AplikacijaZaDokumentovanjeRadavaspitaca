import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { RadniStatus } from 'app/entities/enumerations/radni-status.model';
import { IPodaciORoditeljima, PodaciORoditeljima } from '../podaci-o-roditeljima.model';

import { PodaciORoditeljimaService } from './podaci-o-roditeljima.service';

describe('PodaciORoditeljima Service', () => {
  let service: PodaciORoditeljimaService;
  let httpMock: HttpTestingController;
  let elemDefault: IPodaciORoditeljima;
  let expectedResult: IPodaciORoditeljima | IPodaciORoditeljima[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PodaciORoditeljimaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      jmbg: 'AAAAAAA',
      ime: 'AAAAAAA',
      prezime: 'AAAAAAA',
      telefon: 'AAAAAAA',
      firma: 'AAAAAAA',
      radnoVreme: 'AAAAAAA',
      radniStatus: RadniStatus.NOV,
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

    it('should create a PodaciORoditeljima', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PodaciORoditeljima()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PodaciORoditeljima', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          jmbg: 'BBBBBB',
          ime: 'BBBBBB',
          prezime: 'BBBBBB',
          telefon: 'BBBBBB',
          firma: 'BBBBBB',
          radnoVreme: 'BBBBBB',
          radniStatus: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PodaciORoditeljima', () => {
      const patchObject = Object.assign(
        {
          jmbg: 'BBBBBB',
          ime: 'BBBBBB',
          prezime: 'BBBBBB',
        },
        new PodaciORoditeljima()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PodaciORoditeljima', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          jmbg: 'BBBBBB',
          ime: 'BBBBBB',
          prezime: 'BBBBBB',
          telefon: 'BBBBBB',
          firma: 'BBBBBB',
          radnoVreme: 'BBBBBB',
          radniStatus: 'BBBBBB',
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

    it('should delete a PodaciORoditeljima', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPodaciORoditeljimaToCollectionIfMissing', () => {
      it('should add a PodaciORoditeljima to an empty array', () => {
        const podaciORoditeljima: IPodaciORoditeljima = { id: 123 };
        expectedResult = service.addPodaciORoditeljimaToCollectionIfMissing([], podaciORoditeljima);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(podaciORoditeljima);
      });

      it('should not add a PodaciORoditeljima to an array that contains it', () => {
        const podaciORoditeljima: IPodaciORoditeljima = { id: 123 };
        const podaciORoditeljimaCollection: IPodaciORoditeljima[] = [
          {
            ...podaciORoditeljima,
          },
          { id: 456 },
        ];
        expectedResult = service.addPodaciORoditeljimaToCollectionIfMissing(podaciORoditeljimaCollection, podaciORoditeljima);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PodaciORoditeljima to an array that doesn't contain it", () => {
        const podaciORoditeljima: IPodaciORoditeljima = { id: 123 };
        const podaciORoditeljimaCollection: IPodaciORoditeljima[] = [{ id: 456 }];
        expectedResult = service.addPodaciORoditeljimaToCollectionIfMissing(podaciORoditeljimaCollection, podaciORoditeljima);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(podaciORoditeljima);
      });

      it('should add only unique PodaciORoditeljima to an array', () => {
        const podaciORoditeljimaArray: IPodaciORoditeljima[] = [{ id: 123 }, { id: 456 }, { id: 54998 }];
        const podaciORoditeljimaCollection: IPodaciORoditeljima[] = [{ id: 123 }];
        expectedResult = service.addPodaciORoditeljimaToCollectionIfMissing(podaciORoditeljimaCollection, ...podaciORoditeljimaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const podaciORoditeljima: IPodaciORoditeljima = { id: 123 };
        const podaciORoditeljima2: IPodaciORoditeljima = { id: 456 };
        expectedResult = service.addPodaciORoditeljimaToCollectionIfMissing([], podaciORoditeljima, podaciORoditeljima2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(podaciORoditeljima);
        expect(expectedResult).toContain(podaciORoditeljima2);
      });

      it('should accept null and undefined values', () => {
        const podaciORoditeljima: IPodaciORoditeljima = { id: 123 };
        expectedResult = service.addPodaciORoditeljimaToCollectionIfMissing([], null, podaciORoditeljima, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(podaciORoditeljima);
      });

      it('should return initial array if no PodaciORoditeljima is added', () => {
        const podaciORoditeljimaCollection: IPodaciORoditeljima[] = [{ id: 123 }];
        expectedResult = service.addPodaciORoditeljimaToCollectionIfMissing(podaciORoditeljimaCollection, undefined, null);
        expect(expectedResult).toEqual(podaciORoditeljimaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
