import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PodaciORoditeljimaService } from '../service/podaci-o-roditeljima.service';
import { IPodaciORoditeljima, PodaciORoditeljima } from '../podaci-o-roditeljima.model';
import { IFormular } from 'app/entities/formular/formular.model';
import { FormularService } from 'app/entities/formular/service/formular.service';

import { PodaciORoditeljimaUpdateComponent } from './podaci-o-roditeljima-update.component';

describe('PodaciORoditeljima Management Update Component', () => {
  let comp: PodaciORoditeljimaUpdateComponent;
  let fixture: ComponentFixture<PodaciORoditeljimaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let podaciORoditeljimaService: PodaciORoditeljimaService;
  let formularService: FormularService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PodaciORoditeljimaUpdateComponent],
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
      .overrideTemplate(PodaciORoditeljimaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PodaciORoditeljimaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    podaciORoditeljimaService = TestBed.inject(PodaciORoditeljimaService);
    formularService = TestBed.inject(FormularService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Formular query and add missing value', () => {
      const podaciORoditeljima: IPodaciORoditeljima = { id: 456 };
      const formular: IFormular = { id: 64829 };
      podaciORoditeljima.formular = formular;

      const formularCollection: IFormular[] = [{ id: 72553 }];
      jest.spyOn(formularService, 'query').mockReturnValue(of(new HttpResponse({ body: formularCollection })));
      const additionalFormulars = [formular];
      const expectedCollection: IFormular[] = [...additionalFormulars, ...formularCollection];
      jest.spyOn(formularService, 'addFormularToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ podaciORoditeljima });
      comp.ngOnInit();

      expect(formularService.query).toHaveBeenCalled();
      expect(formularService.addFormularToCollectionIfMissing).toHaveBeenCalledWith(formularCollection, ...additionalFormulars);
      expect(comp.formularsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const podaciORoditeljima: IPodaciORoditeljima = { id: 456 };
      const formular: IFormular = { id: 61792 };
      podaciORoditeljima.formular = formular;

      activatedRoute.data = of({ podaciORoditeljima });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(podaciORoditeljima));
      expect(comp.formularsSharedCollection).toContain(formular);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PodaciORoditeljima>>();
      const podaciORoditeljima = { id: 123 };
      jest.spyOn(podaciORoditeljimaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ podaciORoditeljima });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: podaciORoditeljima }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(podaciORoditeljimaService.update).toHaveBeenCalledWith(podaciORoditeljima);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PodaciORoditeljima>>();
      const podaciORoditeljima = new PodaciORoditeljima();
      jest.spyOn(podaciORoditeljimaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ podaciORoditeljima });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: podaciORoditeljima }));
      saveSubject.complete();

      // THEN
      expect(podaciORoditeljimaService.create).toHaveBeenCalledWith(podaciORoditeljima);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PodaciORoditeljima>>();
      const podaciORoditeljima = { id: 123 };
      jest.spyOn(podaciORoditeljimaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ podaciORoditeljima });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(podaciORoditeljimaService.update).toHaveBeenCalledWith(podaciORoditeljima);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFormularById', () => {
      it('Should return tracked Formular primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFormularById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
