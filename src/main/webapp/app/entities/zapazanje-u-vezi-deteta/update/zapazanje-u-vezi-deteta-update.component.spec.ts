import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ZapazanjeUVeziDetetaService } from '../service/zapazanje-u-vezi-deteta.service';
import { IZapazanjeUVeziDeteta, ZapazanjeUVeziDeteta } from '../zapazanje-u-vezi-deteta.model';
import { IVaspitac } from 'app/entities/vaspitac/vaspitac.model';
import { VaspitacService } from 'app/entities/vaspitac/service/vaspitac.service';
import { IDete } from 'app/entities/dete/dete.model';
import { DeteService } from 'app/entities/dete/service/dete.service';

import { ZapazanjeUVeziDetetaUpdateComponent } from './zapazanje-u-vezi-deteta-update.component';

describe('ZapazanjeUVeziDeteta Management Update Component', () => {
  let comp: ZapazanjeUVeziDetetaUpdateComponent;
  let fixture: ComponentFixture<ZapazanjeUVeziDetetaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let zapazanjeUVeziDetetaService: ZapazanjeUVeziDetetaService;
  let vaspitacService: VaspitacService;
  let deteService: DeteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ZapazanjeUVeziDetetaUpdateComponent],
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
      .overrideTemplate(ZapazanjeUVeziDetetaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ZapazanjeUVeziDetetaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    zapazanjeUVeziDetetaService = TestBed.inject(ZapazanjeUVeziDetetaService);
    vaspitacService = TestBed.inject(VaspitacService);
    deteService = TestBed.inject(DeteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call vaspitac query and add missing value', () => {
      const zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta = { id: 456 };
      const vaspitac: IVaspitac = { id: 20111 };
      zapazanjeUVeziDeteta.vaspitac = vaspitac;

      const vaspitacCollection: IVaspitac[] = [{ id: 38648 }];
      jest.spyOn(vaspitacService, 'query').mockReturnValue(of(new HttpResponse({ body: vaspitacCollection })));
      const expectedCollection: IVaspitac[] = [vaspitac, ...vaspitacCollection];
      jest.spyOn(vaspitacService, 'addVaspitacToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ zapazanjeUVeziDeteta });
      comp.ngOnInit();

      expect(vaspitacService.query).toHaveBeenCalled();
      expect(vaspitacService.addVaspitacToCollectionIfMissing).toHaveBeenCalledWith(vaspitacCollection, vaspitac);
      expect(comp.vaspitacsCollection).toEqual(expectedCollection);
    });

    it('Should call Dete query and add missing value', () => {
      const zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta = { id: 456 };
      const dete: IDete = { id: 4446 };
      zapazanjeUVeziDeteta.dete = dete;

      const deteCollection: IDete[] = [{ id: 65275 }];
      jest.spyOn(deteService, 'query').mockReturnValue(of(new HttpResponse({ body: deteCollection })));
      const additionalDetes = [dete];
      const expectedCollection: IDete[] = [...additionalDetes, ...deteCollection];
      jest.spyOn(deteService, 'addDeteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ zapazanjeUVeziDeteta });
      comp.ngOnInit();

      expect(deteService.query).toHaveBeenCalled();
      expect(deteService.addDeteToCollectionIfMissing).toHaveBeenCalledWith(deteCollection, ...additionalDetes);
      expect(comp.detesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta = { id: 456 };
      const vaspitac: IVaspitac = { id: 93033 };
      zapazanjeUVeziDeteta.vaspitac = vaspitac;
      const dete: IDete = { id: 98011 };
      zapazanjeUVeziDeteta.dete = dete;

      activatedRoute.data = of({ zapazanjeUVeziDeteta });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(zapazanjeUVeziDeteta));
      expect(comp.vaspitacsCollection).toContain(vaspitac);
      expect(comp.detesSharedCollection).toContain(dete);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ZapazanjeUVeziDeteta>>();
      const zapazanjeUVeziDeteta = { id: 123 };
      jest.spyOn(zapazanjeUVeziDetetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ zapazanjeUVeziDeteta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: zapazanjeUVeziDeteta }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(zapazanjeUVeziDetetaService.update).toHaveBeenCalledWith(zapazanjeUVeziDeteta);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ZapazanjeUVeziDeteta>>();
      const zapazanjeUVeziDeteta = new ZapazanjeUVeziDeteta();
      jest.spyOn(zapazanjeUVeziDetetaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ zapazanjeUVeziDeteta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: zapazanjeUVeziDeteta }));
      saveSubject.complete();

      // THEN
      expect(zapazanjeUVeziDetetaService.create).toHaveBeenCalledWith(zapazanjeUVeziDeteta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ZapazanjeUVeziDeteta>>();
      const zapazanjeUVeziDeteta = { id: 123 };
      jest.spyOn(zapazanjeUVeziDetetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ zapazanjeUVeziDeteta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(zapazanjeUVeziDetetaService.update).toHaveBeenCalledWith(zapazanjeUVeziDeteta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackVaspitacById', () => {
      it('Should return tracked Vaspitac primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVaspitacById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDeteById', () => {
      it('Should return tracked Dete primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDeteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
