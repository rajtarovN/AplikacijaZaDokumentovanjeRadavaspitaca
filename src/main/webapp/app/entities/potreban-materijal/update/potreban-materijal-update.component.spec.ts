import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PotrebanMaterijalService } from '../service/potreban-materijal.service';
import { IPotrebanMaterijal, PotrebanMaterijal } from '../potreban-materijal.model';
import { IObjekat } from 'app/entities/objekat/objekat.model';
import { ObjekatService } from 'app/entities/objekat/service/objekat.service';

import { PotrebanMaterijalUpdateComponent } from './potreban-materijal-update.component';

describe('PotrebanMaterijal Management Update Component', () => {
  let comp: PotrebanMaterijalUpdateComponent;
  let fixture: ComponentFixture<PotrebanMaterijalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let potrebanMaterijalService: PotrebanMaterijalService;
  let objekatService: ObjekatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PotrebanMaterijalUpdateComponent],
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
      .overrideTemplate(PotrebanMaterijalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PotrebanMaterijalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    potrebanMaterijalService = TestBed.inject(PotrebanMaterijalService);
    objekatService = TestBed.inject(ObjekatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Objekat query and add missing value', () => {
      const potrebanMaterijal: IPotrebanMaterijal = { id: 456 };
      const objekat: IObjekat = { id: 12301 };
      potrebanMaterijal.objekat = objekat;

      const objekatCollection: IObjekat[] = [{ id: 50756 }];
      jest.spyOn(objekatService, 'query').mockReturnValue(of(new HttpResponse({ body: objekatCollection })));
      const additionalObjekats = [objekat];
      const expectedCollection: IObjekat[] = [...additionalObjekats, ...objekatCollection];
      jest.spyOn(objekatService, 'addObjekatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ potrebanMaterijal });
      comp.ngOnInit();

      expect(objekatService.query).toHaveBeenCalled();
      expect(objekatService.addObjekatToCollectionIfMissing).toHaveBeenCalledWith(objekatCollection, ...additionalObjekats);
      expect(comp.objekatsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const potrebanMaterijal: IPotrebanMaterijal = { id: 456 };
      const objekat: IObjekat = { id: 45751 };
      potrebanMaterijal.objekat = objekat;

      activatedRoute.data = of({ potrebanMaterijal });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(potrebanMaterijal));
      expect(comp.objekatsSharedCollection).toContain(objekat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PotrebanMaterijal>>();
      const potrebanMaterijal = { id: 123 };
      jest.spyOn(potrebanMaterijalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ potrebanMaterijal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: potrebanMaterijal }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(potrebanMaterijalService.update).toHaveBeenCalledWith(potrebanMaterijal);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PotrebanMaterijal>>();
      const potrebanMaterijal = new PotrebanMaterijal();
      jest.spyOn(potrebanMaterijalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ potrebanMaterijal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: potrebanMaterijal }));
      saveSubject.complete();

      // THEN
      expect(potrebanMaterijalService.create).toHaveBeenCalledWith(potrebanMaterijal);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PotrebanMaterijal>>();
      const potrebanMaterijal = { id: 123 };
      jest.spyOn(potrebanMaterijalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ potrebanMaterijal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(potrebanMaterijalService.update).toHaveBeenCalledWith(potrebanMaterijal);
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
