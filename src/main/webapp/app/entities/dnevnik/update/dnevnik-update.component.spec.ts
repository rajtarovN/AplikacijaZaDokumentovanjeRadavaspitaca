import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DnevnikService } from '../service/dnevnik.service';
import { IDnevnik, Dnevnik } from '../dnevnik.model';
import { IGrupa } from 'app/entities/grupa/grupa.model';
import { GrupaService } from 'app/entities/grupa/service/grupa.service';
import { IVaspitac } from 'app/entities/vaspitac/vaspitac.model';
import { VaspitacService } from 'app/entities/vaspitac/service/vaspitac.service';

import { DnevnikUpdateComponent } from './dnevnik-update.component';

describe('Dnevnik Management Update Component', () => {
  let comp: DnevnikUpdateComponent;
  let fixture: ComponentFixture<DnevnikUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dnevnikService: DnevnikService;
  let grupaService: GrupaService;
  let vaspitacService: VaspitacService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DnevnikUpdateComponent],
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
      .overrideTemplate(DnevnikUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DnevnikUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dnevnikService = TestBed.inject(DnevnikService);
    grupaService = TestBed.inject(GrupaService);
    vaspitacService = TestBed.inject(VaspitacService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call grupa query and add missing value', () => {
      const dnevnik: IDnevnik = { id: 456 };
      const grupa: IGrupa = { id: 84359 };
      dnevnik.grupa = grupa;

      const grupaCollection: IGrupa[] = [{ id: 56370 }];
      jest.spyOn(grupaService, 'query').mockReturnValue(of(new HttpResponse({ body: grupaCollection })));
      const expectedCollection: IGrupa[] = [grupa, ...grupaCollection];
      jest.spyOn(grupaService, 'addGrupaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dnevnik });
      comp.ngOnInit();

      expect(grupaService.query).toHaveBeenCalled();
      expect(grupaService.addGrupaToCollectionIfMissing).toHaveBeenCalledWith(grupaCollection, grupa);
      expect(comp.grupasCollection).toEqual(expectedCollection);
    });

    it('Should call Vaspitac query and add missing value', () => {
      const dnevnik: IDnevnik = { id: 456 };
      const vaspitacs: IVaspitac[] = [{ id: 78986 }];
      dnevnik.vaspitacs = vaspitacs;

      const vaspitacCollection: IVaspitac[] = [{ id: 18656 }];
      jest.spyOn(vaspitacService, 'query').mockReturnValue(of(new HttpResponse({ body: vaspitacCollection })));
      const additionalVaspitacs = [...vaspitacs];
      const expectedCollection: IVaspitac[] = [...additionalVaspitacs, ...vaspitacCollection];
      jest.spyOn(vaspitacService, 'addVaspitacToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dnevnik });
      comp.ngOnInit();

      expect(vaspitacService.query).toHaveBeenCalled();
      expect(vaspitacService.addVaspitacToCollectionIfMissing).toHaveBeenCalledWith(vaspitacCollection, ...additionalVaspitacs);
      expect(comp.vaspitacsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dnevnik: IDnevnik = { id: 456 };
      const grupa: IGrupa = { id: 80470 };
      dnevnik.grupa = grupa;
      const vaspitacs: IVaspitac = { id: 52872 };
      dnevnik.vaspitacs = [vaspitacs];

      activatedRoute.data = of({ dnevnik });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(dnevnik));
      expect(comp.grupasCollection).toContain(grupa);
      expect(comp.vaspitacsSharedCollection).toContain(vaspitacs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dnevnik>>();
      const dnevnik = { id: 123 };
      jest.spyOn(dnevnikService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dnevnik });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dnevnik }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dnevnikService.update).toHaveBeenCalledWith(dnevnik);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dnevnik>>();
      const dnevnik = new Dnevnik();
      jest.spyOn(dnevnikService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dnevnik });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dnevnik }));
      saveSubject.complete();

      // THEN
      expect(dnevnikService.create).toHaveBeenCalledWith(dnevnik);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dnevnik>>();
      const dnevnik = { id: 123 };
      jest.spyOn(dnevnikService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dnevnik });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dnevnikService.update).toHaveBeenCalledWith(dnevnik);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackGrupaById', () => {
      it('Should return tracked Grupa primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGrupaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackVaspitacById', () => {
      it('Should return tracked Vaspitac primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVaspitacById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedVaspitac', () => {
      it('Should return option if no Vaspitac is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedVaspitac(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Vaspitac for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedVaspitac(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Vaspitac is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedVaspitac(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
