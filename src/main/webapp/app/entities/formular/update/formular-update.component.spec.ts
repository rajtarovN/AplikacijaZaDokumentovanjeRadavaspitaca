import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FormularService } from '../service/formular.service';
import { IFormular, Formular } from '../formular.model';
import { IAdresa } from 'app/entities/adresa/adresa.model';
import { AdresaService } from 'app/entities/adresa/service/adresa.service';
import { IRoditelj } from 'app/entities/roditelj/roditelj.model';
import { RoditeljService } from 'app/entities/roditelj/service/roditelj.service';

import { FormularUpdateComponent } from './formular-update.component';

describe('Formular Management Update Component', () => {
  let comp: FormularUpdateComponent;
  let fixture: ComponentFixture<FormularUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formularService: FormularService;
  let adresaService: AdresaService;
  let roditeljService: RoditeljService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FormularUpdateComponent],
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
      .overrideTemplate(FormularUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormularUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formularService = TestBed.inject(FormularService);
    adresaService = TestBed.inject(AdresaService);
    roditeljService = TestBed.inject(RoditeljService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call adresa query and add missing value', () => {
      const formular: IFormular = { id: 456 };
      const adresa: IAdresa = { id: 69939 };
      formular.adresa = adresa;

      const adresaCollection: IAdresa[] = [{ id: 63798 }];
      jest.spyOn(adresaService, 'query').mockReturnValue(of(new HttpResponse({ body: adresaCollection })));
      const expectedCollection: IAdresa[] = [adresa, ...adresaCollection];
      jest.spyOn(adresaService, 'addAdresaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formular });
      comp.ngOnInit();

      expect(adresaService.query).toHaveBeenCalled();
      expect(adresaService.addAdresaToCollectionIfMissing).toHaveBeenCalledWith(adresaCollection, adresa);
      expect(comp.adresasCollection).toEqual(expectedCollection);
    });

    it('Should call Roditelj query and add missing value', () => {
      const formular: IFormular = { id: 456 };
      const roditelj: IRoditelj = { id: 15875 };
      formular.roditelj = roditelj;

      const roditeljCollection: IRoditelj[] = [{ id: 56493 }];
      jest.spyOn(roditeljService, 'query').mockReturnValue(of(new HttpResponse({ body: roditeljCollection })));
      const additionalRoditeljs = [roditelj];
      const expectedCollection: IRoditelj[] = [...additionalRoditeljs, ...roditeljCollection];
      jest.spyOn(roditeljService, 'addRoditeljToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formular });
      comp.ngOnInit();

      expect(roditeljService.query).toHaveBeenCalled();
      expect(roditeljService.addRoditeljToCollectionIfMissing).toHaveBeenCalledWith(roditeljCollection, ...additionalRoditeljs);
      expect(comp.roditeljsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const formular: IFormular = { id: 456 };
      const adresa: IAdresa = { id: 41273 };
      formular.adresa = adresa;
      const roditelj: IRoditelj = { id: 51107 };
      formular.roditelj = roditelj;

      activatedRoute.data = of({ formular });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(formular));
      expect(comp.adresasCollection).toContain(adresa);
      expect(comp.roditeljsSharedCollection).toContain(roditelj);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Formular>>();
      const formular = { id: 123 };
      jest.spyOn(formularService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formular });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formular }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(formularService.update).toHaveBeenCalledWith(formular);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Formular>>();
      const formular = new Formular();
      jest.spyOn(formularService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formular });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formular }));
      saveSubject.complete();

      // THEN
      expect(formularService.create).toHaveBeenCalledWith(formular);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Formular>>();
      const formular = { id: 123 };
      jest.spyOn(formularService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formular });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formularService.update).toHaveBeenCalledWith(formular);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAdresaById', () => {
      it('Should return tracked Adresa primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAdresaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackRoditeljById', () => {
      it('Should return tracked Roditelj primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRoditeljById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
