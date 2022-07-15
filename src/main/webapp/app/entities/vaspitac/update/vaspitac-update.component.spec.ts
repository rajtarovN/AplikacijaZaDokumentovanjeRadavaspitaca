import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VaspitacService } from '../service/vaspitac.service';
import { IVaspitac, Vaspitac } from '../vaspitac.model';
import { IObjekat } from 'app/entities/objekat/objekat.model';
import { ObjekatService } from 'app/entities/objekat/service/objekat.service';

import { VaspitacUpdateComponent } from './vaspitac-update.component';

describe('Vaspitac Management Update Component', () => {
  let comp: VaspitacUpdateComponent;
  let fixture: ComponentFixture<VaspitacUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vaspitacService: VaspitacService;
  let objekatService: ObjekatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VaspitacUpdateComponent],
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
      .overrideTemplate(VaspitacUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VaspitacUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vaspitacService = TestBed.inject(VaspitacService);
    objekatService = TestBed.inject(ObjekatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Objekat query and add missing value', () => {
      const vaspitac: IVaspitac = { id: 456 };
      const objekat: IObjekat = { id: 79433 };
      vaspitac.objekat = objekat;

      const objekatCollection: IObjekat[] = [{ id: 43735 }];
      jest.spyOn(objekatService, 'query').mockReturnValue(of(new HttpResponse({ body: objekatCollection })));
      const additionalObjekats = [objekat];
      const expectedCollection: IObjekat[] = [...additionalObjekats, ...objekatCollection];
      jest.spyOn(objekatService, 'addObjekatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vaspitac });
      comp.ngOnInit();

      expect(objekatService.query).toHaveBeenCalled();
      expect(objekatService.addObjekatToCollectionIfMissing).toHaveBeenCalledWith(objekatCollection, ...additionalObjekats);
      expect(comp.objekatsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vaspitac: IVaspitac = { id: 456 };
      const objekat: IObjekat = { id: 73841 };
      vaspitac.objekat = objekat;

      activatedRoute.data = of({ vaspitac });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(vaspitac));
      expect(comp.objekatsSharedCollection).toContain(objekat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vaspitac>>();
      const vaspitac = { id: 123 };
      jest.spyOn(vaspitacService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaspitac });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vaspitac }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(vaspitacService.update).toHaveBeenCalledWith(vaspitac);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vaspitac>>();
      const vaspitac = new Vaspitac();
      jest.spyOn(vaspitacService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaspitac });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vaspitac }));
      saveSubject.complete();

      // THEN
      expect(vaspitacService.create).toHaveBeenCalledWith(vaspitac);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vaspitac>>();
      const vaspitac = { id: 123 };
      jest.spyOn(vaspitacService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaspitac });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vaspitacService.update).toHaveBeenCalledWith(vaspitac);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackObjekatById', () => {
      it('Should return tracked Objekat primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackObjekatById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
