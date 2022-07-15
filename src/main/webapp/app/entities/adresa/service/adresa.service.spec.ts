import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAdresa, Adresa } from '../adresa.model';

import { AdresaService } from './adresa.service';

describe('Adresa Service', () => {
  let service: AdresaService;
  let httpMock: HttpTestingController;
  let elemDefault: IAdresa;
  let expectedResult: IAdresa | IAdresa[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AdresaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      mesto: 'AAAAAAA',
      ulica: 'AAAAAAA',
      broj: 'AAAAAAA',
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

    it('should create a Adresa', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Adresa()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Adresa', () => {
      const returnedFromService = Object.assign(
        {
          mesto: 'BBBBBB',
          ulica: 'BBBBBB',
          broj: 'BBBBBB',
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

    it('should partial update a Adresa', () => {
      const patchObject = Object.assign(
        {
          mesto: 'BBBBBB',
        },
        new Adresa()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Adresa', () => {
      const returnedFromService = Object.assign(
        {
          mesto: 'BBBBBB',
          ulica: 'BBBBBB',
          broj: 'BBBBBB',
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

    it('should delete a Adresa', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAdresaToCollectionIfMissing', () => {
      it('should add a Adresa to an empty array', () => {
        const adresa: IAdresa = { id: 123 };
        expectedResult = service.addAdresaToCollectionIfMissing([], adresa);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(adresa);
      });

      it('should not add a Adresa to an array that contains it', () => {
        const adresa: IAdresa = { id: 123 };
        const adresaCollection: IAdresa[] = [
          {
            ...adresa,
          },
          { id: 456 },
        ];
        expectedResult = service.addAdresaToCollectionIfMissing(adresaCollection, adresa);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Adresa to an array that doesn't contain it", () => {
        const adresa: IAdresa = { id: 123 };
        const adresaCollection: IAdresa[] = [{ id: 456 }];
        expectedResult = service.addAdresaToCollectionIfMissing(adresaCollection, adresa);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(adresa);
      });

      it('should add only unique Adresa to an array', () => {
        const adresaArray: IAdresa[] = [{ id: 123 }, { id: 456 }, { id: 27047 }];
        const adresaCollection: IAdresa[] = [{ id: 123 }];
        expectedResult = service.addAdresaToCollectionIfMissing(adresaCollection, ...adresaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const adresa: IAdresa = { id: 123 };
        const adresa2: IAdresa = { id: 456 };
        expectedResult = service.addAdresaToCollectionIfMissing([], adresa, adresa2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(adresa);
        expect(expectedResult).toContain(adresa2);
      });

      it('should accept null and undefined values', () => {
        const adresa: IAdresa = { id: 123 };
        expectedResult = service.addAdresaToCollectionIfMissing([], null, adresa, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(adresa);
      });

      it('should return initial array if no Adresa is added', () => {
        const adresaCollection: IAdresa[] = [{ id: 123 }];
        expectedResult = service.addAdresaToCollectionIfMissing(adresaCollection, undefined, null);
        expect(expectedResult).toEqual(adresaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
