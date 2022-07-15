import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TipGrupe } from 'app/entities/enumerations/tip-grupe.model';
import { IGrupa, Grupa } from '../grupa.model';

import { GrupaService } from './grupa.service';

describe('Grupa Service', () => {
  let service: GrupaService;
  let httpMock: HttpTestingController;
  let elemDefault: IGrupa;
  let expectedResult: IGrupa | IGrupa[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GrupaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      tipGrupe: TipGrupe.POLUDNEVNA,
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

    it('should create a Grupa', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Grupa()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Grupa', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tipGrupe: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Grupa', () => {
      const patchObject = Object.assign(
        {
          tipGrupe: 'BBBBBB',
        },
        new Grupa()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Grupa', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tipGrupe: 'BBBBBB',
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

    it('should delete a Grupa', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGrupaToCollectionIfMissing', () => {
      it('should add a Grupa to an empty array', () => {
        const grupa: IGrupa = { id: 123 };
        expectedResult = service.addGrupaToCollectionIfMissing([], grupa);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(grupa);
      });

      it('should not add a Grupa to an array that contains it', () => {
        const grupa: IGrupa = { id: 123 };
        const grupaCollection: IGrupa[] = [
          {
            ...grupa,
          },
          { id: 456 },
        ];
        expectedResult = service.addGrupaToCollectionIfMissing(grupaCollection, grupa);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Grupa to an array that doesn't contain it", () => {
        const grupa: IGrupa = { id: 123 };
        const grupaCollection: IGrupa[] = [{ id: 456 }];
        expectedResult = service.addGrupaToCollectionIfMissing(grupaCollection, grupa);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(grupa);
      });

      it('should add only unique Grupa to an array', () => {
        const grupaArray: IGrupa[] = [{ id: 123 }, { id: 456 }, { id: 68594 }];
        const grupaCollection: IGrupa[] = [{ id: 123 }];
        expectedResult = service.addGrupaToCollectionIfMissing(grupaCollection, ...grupaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const grupa: IGrupa = { id: 123 };
        const grupa2: IGrupa = { id: 456 };
        expectedResult = service.addGrupaToCollectionIfMissing([], grupa, grupa2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(grupa);
        expect(expectedResult).toContain(grupa2);
      });

      it('should accept null and undefined values', () => {
        const grupa: IGrupa = { id: 123 };
        expectedResult = service.addGrupaToCollectionIfMissing([], null, grupa, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(grupa);
      });

      it('should return initial array if no Grupa is added', () => {
        const grupaCollection: IGrupa[] = [{ id: 123 }];
        expectedResult = service.addGrupaToCollectionIfMissing(grupaCollection, undefined, null);
        expect(expectedResult).toEqual(grupaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
