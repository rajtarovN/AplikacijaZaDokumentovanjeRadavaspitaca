import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeteService } from '../service/dete.service';
import { IDete, Dete } from '../dete.model';
import { IFormular } from 'app/entities/formular/formular.model';
import { FormularService } from 'app/entities/formular/service/formular.service';
import { IRoditelj } from 'app/entities/roditelj/roditelj.model';
import { RoditeljService } from 'app/entities/roditelj/service/roditelj.service';
import { IGrupa } from 'app/entities/grupa/grupa.model';
import { GrupaService } from 'app/entities/grupa/service/grupa.service';

import { DeteUpdateComponent } from './dete-update.component';

describe('Dete Management Update Component', () => {
  let comp: DeteUpdateComponent;
  let fixture: ComponentFixture<DeteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deteService: DeteService;
  let formularService: FormularService;
  let roditeljService: RoditeljService;
  let grupaService: GrupaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeteUpdateComponent],
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
      .overrideTemplate(DeteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deteService = TestBed.inject(DeteService);
    formularService = TestBed.inject(FormularService);
    roditeljService = TestBed.inject(RoditeljService);
    grupaService = TestBed.inject(GrupaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call formular query and add missing value', () => {
      const dete: IDete = { id: 456 };
      const formular: IFormular = { id: 70445 };
      dete.formular = formular;

      const formularCollection: IFormular[] = [{ id: 89942 }];
      jest.spyOn(formularService, 'query').mockReturnValue(of(new HttpResponse({ body: formularCollection })));
      const expectedCollection: IFormular[] = [formular, ...formularCollection];
      jest.spyOn(formularService, 'addFormularToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dete });
      comp.ngOnInit();

      expect(formularService.query).toHaveBeenCalled();
      expect(formularService.addFormularToCollectionIfMissing).toHaveBeenCalledWith(formularCollection, formular);
      expect(comp.formularsCollection).toEqual(expectedCollection);
    });

    it('Should call Roditelj query and add missing value', () => {
      const dete: IDete = { id: 456 };
      const roditelj: IRoditelj = { id: 31360 };
      dete.roditelj = roditelj;

      const roditeljCollection: IRoditelj[] = [{ id: 93076 }];
      jest.spyOn(roditeljService, 'query').mockReturnValue(of(new HttpResponse({ body: roditeljCollection })));
      const additionalRoditeljs = [roditelj];
      const expectedCollection: IRoditelj[] = [...additionalRoditeljs, ...roditeljCollection];
      jest.spyOn(roditeljService, 'addRoditeljToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dete });
      comp.ngOnInit();

      expect(roditeljService.query).toHaveBeenCalled();
      expect(roditeljService.addRoditeljToCollectionIfMissing).toHaveBeenCalledWith(roditeljCollection, ...additionalRoditeljs);
      expect(comp.roditeljsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Grupa query and add missing value', () => {
      const dete: IDete = { id: 456 };
      const grupa: IGrupa = { id: 57016 };
      dete.grupa = grupa;

      const grupaCollection: IGrupa[] = [{ id: 83681 }];
      jest.spyOn(grupaService, 'query').mockReturnValue(of(new HttpResponse({ body: grupaCollection })));
      const additionalGrupas = [grupa];
      const expectedCollection: IGrupa[] = [...additionalGrupas, ...grupaCollection];
      jest.spyOn(grupaService, 'addGrupaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dete });
      comp.ngOnInit();

      expect(grupaService.query).toHaveBeenCalled();
      expect(grupaService.addGrupaToCollectionIfMissing).toHaveBeenCalledWith(grupaCollection, ...additionalGrupas);
      expect(comp.grupasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dete: IDete = { id: 456 };
      const formular: IFormular = { id: 7229 };
      dete.formular = formular;
      const roditelj: IRoditelj = { id: 69964 };
      dete.roditelj = roditelj;
      const grupa: IGrupa = { id: 34579 };
      dete.grupa = grupa;

      activatedRoute.data = of({ dete });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(dete));
      expect(comp.formularsCollection).toContain(formular);
      expect(comp.roditeljsSharedCollection).toContain(roditelj);
      expect(comp.grupasSharedCollection).toContain(grupa);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dete>>();
      const dete = { id: 123 };
      jest.spyOn(deteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dete }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(deteService.update).toHaveBeenCalledWith(dete);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dete>>();
      const dete = new Dete();
      jest.spyOn(deteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dete }));
      saveSubject.complete();

      // THEN
      expect(deteService.create).toHaveBeenCalledWith(dete);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dete>>();
      const dete = { id: 123 };
      jest.spyOn(deteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deteService.update).toHaveBeenCalledWith(dete);
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

    describe('trackRoditeljById', () => {
      it('Should return tracked Roditelj primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRoditeljById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackGrupaById', () => {
      it('Should return tracked Grupa primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGrupaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
