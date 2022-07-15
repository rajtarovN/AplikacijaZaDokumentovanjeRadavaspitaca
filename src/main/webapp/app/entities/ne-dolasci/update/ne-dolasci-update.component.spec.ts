import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NeDolasciService } from '../service/ne-dolasci.service';
import { INeDolasci, NeDolasci } from '../ne-dolasci.model';
import { IDete } from 'app/entities/dete/dete.model';
import { DeteService } from 'app/entities/dete/service/dete.service';
import { IDnevnik } from 'app/entities/dnevnik/dnevnik.model';
import { DnevnikService } from 'app/entities/dnevnik/service/dnevnik.service';

import { NeDolasciUpdateComponent } from './ne-dolasci-update.component';

describe('NeDolasci Management Update Component', () => {
  let comp: NeDolasciUpdateComponent;
  let fixture: ComponentFixture<NeDolasciUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let neDolasciService: NeDolasciService;
  let deteService: DeteService;
  let dnevnikService: DnevnikService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NeDolasciUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(NeDolasciUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NeDolasciUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    neDolasciService = TestBed.inject(NeDolasciService);
    deteService = TestBed.inject(DeteService);
    dnevnikService = TestBed.inject(DnevnikService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call dete query and add missing value', () => {
      const neDolasci: INeDolasci = { id: 456 };
      const dete: IDete = { id: 20152 };
      neDolasci.dete = dete;

      const deteCollection: IDete[] = [{ id: 28229 }];
      jest.spyOn(deteService, 'query').mockReturnValue(of(new HttpResponse({ body: deteCollection })));
      const expectedCollection: IDete[] = [dete, ...deteCollection];
      jest.spyOn(deteService, 'addDeteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ neDolasci });
      comp.ngOnInit();

      expect(deteService.query).toHaveBeenCalled();
      expect(deteService.addDeteToCollectionIfMissing).toHaveBeenCalledWith(deteCollection, dete);
      expect(comp.detesCollection).toEqual(expectedCollection);
    });

    it('Should call Dnevnik query and add missing value', () => {
      const neDolasci: INeDolasci = { id: 456 };
      const dnevnik: IDnevnik = { id: 58703 };
      neDolasci.dnevnik = dnevnik;

      const dnevnikCollection: IDnevnik[] = [{ id: 80524 }];
      jest.spyOn(dnevnikService, 'query').mockReturnValue(of(new HttpResponse({ body: dnevnikCollection })));
      const additionalDnevniks = [dnevnik];
      const expectedCollection: IDnevnik[] = [...additionalDnevniks, ...dnevnikCollection];
      jest.spyOn(dnevnikService, 'addDnevnikToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ neDolasci });
      comp.ngOnInit();

      expect(dnevnikService.query).toHaveBeenCalled();
      expect(dnevnikService.addDnevnikToCollectionIfMissing).toHaveBeenCalledWith(dnevnikCollection, ...additionalDnevniks);
      expect(comp.dnevniksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const neDolasci: INeDolasci = { id: 456 };
      const dete: IDete = { id: 67787 };
      neDolasci.dete = dete;
      const dnevnik: IDnevnik = { id: 62558 };
      neDolasci.dnevnik = dnevnik;

      activatedRoute.data = of({ neDolasci });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(neDolasci));
      expect(comp.detesCollection).toContain(dete);
      expect(comp.dnevniksSharedCollection).toContain(dnevnik);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NeDolasci>>();
      const neDolasci = { id: 123 };
      jest.spyOn(neDolasciService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ neDolasci });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: neDolasci }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(neDolasciService.update).toHaveBeenCalledWith(neDolasci);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NeDolasci>>();
      const neDolasci = new NeDolasci();
      jest.spyOn(neDolasciService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ neDolasci });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: neDolasci }));
      saveSubject.complete();

      // THEN
      expect(neDolasciService.create).toHaveBeenCalledWith(neDolasci);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NeDolasci>>();
      const neDolasci = { id: 123 };
      jest.spyOn(neDolasciService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ neDolasci });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(neDolasciService.update).toHaveBeenCalledWith(neDolasci);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDeteById', () => {
      it('Should return tracked Dete primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDeteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDnevnikById', () => {
      it('Should return tracked Dnevnik primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDnevnikById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
