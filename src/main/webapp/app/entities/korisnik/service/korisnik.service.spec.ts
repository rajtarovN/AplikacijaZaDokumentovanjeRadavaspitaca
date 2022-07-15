import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IKorisnik, Korisnik } from '../korisnik.model';

import { KorisnikService } from './korisnik.service';

describe('Korisnik Service', () => {
  let service: KorisnikService;
  let httpMock: HttpTestingController;
  let elemDefault: IKorisnik;
  let expectedResult: IKorisnik | IKorisnik[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(KorisnikService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      korisnickoIme: 'AAAAAAA',
      sifra: 'AAAAAAA',
      ime: 'AAAAAAA',
      prezime: 'AAAAAAA',
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

    it('should create a Korisnik', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Korisnik()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Korisnik', () => {
      const returnedFromService = Object.assign(
        {
          korisnickoIme: 'BBBBBB',
          sifra: 'BBBBBB',
          ime: 'BBBBBB',
          prezime: 'BBBBBB',
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

    it('should partial update a Korisnik', () => {
      const patchObject = Object.assign(
        {
          korisnickoIme: 'BBBBBB',
          sifra: 'BBBBBB',
        },
        new Korisnik()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Korisnik', () => {
      const returnedFromService = Object.assign(
        {
          korisnickoIme: 'BBBBBB',
          sifra: 'BBBBBB',
          ime: 'BBBBBB',
          prezime: 'BBBBBB',
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

    it('should delete a Korisnik', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addKorisnikToCollectionIfMissing', () => {
      it('should add a Korisnik to an empty array', () => {
        const korisnik: IKorisnik = { id: 123 };
        expectedResult = service.addKorisnikToCollectionIfMissing([], korisnik);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(korisnik);
      });

      it('should not add a Korisnik to an array that contains it', () => {
        const korisnik: IKorisnik = { id: 123 };
        const korisnikCollection: IKorisnik[] = [
          {
            ...korisnik,
          },
          { id: 456 },
        ];
        expectedResult = service.addKorisnikToCollectionIfMissing(korisnikCollection, korisnik);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Korisnik to an array that doesn't contain it", () => {
        const korisnik: IKorisnik = { id: 123 };
        const korisnikCollection: IKorisnik[] = [{ id: 456 }];
        expectedResult = service.addKorisnikToCollectionIfMissing(korisnikCollection, korisnik);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(korisnik);
      });

      it('should add only unique Korisnik to an array', () => {
        const korisnikArray: IKorisnik[] = [{ id: 123 }, { id: 456 }, { id: 4149 }];
        const korisnikCollection: IKorisnik[] = [{ id: 123 }];
        expectedResult = service.addKorisnikToCollectionIfMissing(korisnikCollection, ...korisnikArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const korisnik: IKorisnik = { id: 123 };
        const korisnik2: IKorisnik = { id: 456 };
        expectedResult = service.addKorisnikToCollectionIfMissing([], korisnik, korisnik2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(korisnik);
        expect(expectedResult).toContain(korisnik2);
      });

      it('should accept null and undefined values', () => {
        const korisnik: IKorisnik = { id: 123 };
        expectedResult = service.addKorisnikToCollectionIfMissing([], null, korisnik, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(korisnik);
      });

      it('should return initial array if no Korisnik is added', () => {
        const korisnikCollection: IKorisnik[] = [{ id: 123 }];
        expectedResult = service.addKorisnikToCollectionIfMissing(korisnikCollection, undefined, null);
        expect(expectedResult).toEqual(korisnikCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
