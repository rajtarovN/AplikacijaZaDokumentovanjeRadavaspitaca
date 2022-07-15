import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { StatusFormulara } from 'app/entities/enumerations/status-formulara.model';
import { TipGrupe } from 'app/entities/enumerations/tip-grupe.model';
import { IFormular, Formular } from '../formular.model';

import { FormularService } from './formular.service';

describe('Formular Service', () => {
  let service: FormularService;
  let httpMock: HttpTestingController;
  let elemDefault: IFormular;
  let expectedResult: IFormular | IFormular[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FormularService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      mesecUpisa: 0,
      jmbg: 'AAAAAAA',
      datumRodjenja: 'AAAAAAA',
      imeDeteta: 'AAAAAAA',
      mestoRodjenja: 'AAAAAAA',
      opstinaRodjenja: 'AAAAAAA',
      drzava: 'AAAAAAA',
      brDeceUPorodici: 0,
      zdravstveniProblemi: 'AAAAAAA',
      zdravstveniUslovi: 'AAAAAAA',
      statusFormulara: StatusFormulara.NOV,
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

    it('should create a Formular', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Formular()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Formular', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          mesecUpisa: 1,
          jmbg: 'BBBBBB',
          datumRodjenja: 'BBBBBB',
          imeDeteta: 'BBBBBB',
          mestoRodjenja: 'BBBBBB',
          opstinaRodjenja: 'BBBBBB',
          drzava: 'BBBBBB',
          brDeceUPorodici: 1,
          zdravstveniProblemi: 'BBBBBB',
          zdravstveniUslovi: 'BBBBBB',
          statusFormulara: 'BBBBBB',
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

    it('should partial update a Formular', () => {
      const patchObject = Object.assign(
        {
          mesecUpisa: 1,
          jmbg: 'BBBBBB',
          datumRodjenja: 'BBBBBB',
          imeDeteta: 'BBBBBB',
          brDeceUPorodici: 1,
          zdravstveniProblemi: 'BBBBBB',
          zdravstveniUslovi: 'BBBBBB',
          tipGrupe: 'BBBBBB',
        },
        new Formular()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Formular', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          mesecUpisa: 1,
          jmbg: 'BBBBBB',
          datumRodjenja: 'BBBBBB',
          imeDeteta: 'BBBBBB',
          mestoRodjenja: 'BBBBBB',
          opstinaRodjenja: 'BBBBBB',
          drzava: 'BBBBBB',
          brDeceUPorodici: 1,
          zdravstveniProblemi: 'BBBBBB',
          zdravstveniUslovi: 'BBBBBB',
          statusFormulara: 'BBBBBB',
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

    it('should delete a Formular', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFormularToCollectionIfMissing', () => {
      it('should add a Formular to an empty array', () => {
        const formular: IFormular = { id: 123 };
        expectedResult = service.addFormularToCollectionIfMissing([], formular);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formular);
      });

      it('should not add a Formular to an array that contains it', () => {
        const formular: IFormular = { id: 123 };
        const formularCollection: IFormular[] = [
          {
            ...formular,
          },
          { id: 456 },
        ];
        expectedResult = service.addFormularToCollectionIfMissing(formularCollection, formular);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Formular to an array that doesn't contain it", () => {
        const formular: IFormular = { id: 123 };
        const formularCollection: IFormular[] = [{ id: 456 }];
        expectedResult = service.addFormularToCollectionIfMissing(formularCollection, formular);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formular);
      });

      it('should add only unique Formular to an array', () => {
        const formularArray: IFormular[] = [{ id: 123 }, { id: 456 }, { id: 82561 }];
        const formularCollection: IFormular[] = [{ id: 123 }];
        expectedResult = service.addFormularToCollectionIfMissing(formularCollection, ...formularArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const formular: IFormular = { id: 123 };
        const formular2: IFormular = { id: 456 };
        expectedResult = service.addFormularToCollectionIfMissing([], formular, formular2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formular);
        expect(expectedResult).toContain(formular2);
      });

      it('should accept null and undefined values', () => {
        const formular: IFormular = { id: 123 };
        expectedResult = service.addFormularToCollectionIfMissing([], null, formular, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formular);
      });

      it('should return initial array if no Formular is added', () => {
        const formularCollection: IFormular[] = [{ id: 123 }];
        expectedResult = service.addFormularToCollectionIfMissing(formularCollection, undefined, null);
        expect(expectedResult).toEqual(formularCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
