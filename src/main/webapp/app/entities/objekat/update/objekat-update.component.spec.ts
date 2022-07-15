import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ObjekatService } from '../service/objekat.service';
import { IObjekat, Objekat } from '../objekat.model';
import { IAdresa } from 'app/entities/adresa/adresa.model';
import { AdresaService } from 'app/entities/adresa/service/adresa.service';

import { ObjekatUpdateComponent } from './objekat-update.component';

describe('Objekat Management Update Component', () => {
  let comp: ObjekatUpdateComponent;
  let fixture: ComponentFixture<ObjekatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let objekatService: ObjekatService;
  let adresaService: AdresaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ObjekatUpdateComponent],
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
      .overrideTemplate(ObjekatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ObjekatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    objekatService = TestBed.inject(ObjekatService);
    adresaService = TestBed.inject(AdresaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call adresa query and add missing value', () => {
      const objekat: IObjekat = { id: 456 };
      const adresa: IAdresa = { id: 94219 };
      objekat.adresa = adresa;

      const adresaCollection: IAdresa[] = [{ id: 13009 }];
      jest.spyOn(adresaService, 'query').mockReturnValue(of(new HttpResponse({ body: adresaCollection })));
      const expectedCollection: IAdresa[] = [adresa, ...adresaCollection];
      jest.spyOn(adresaService, 'addAdresaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ objekat });
      comp.ngOnInit();

      expect(adresaService.query).toHaveBeenCalled();
      expect(adresaService.addAdresaToCollectionIfMissing).toHaveBeenCalledWith(adresaCollection, adresa);
      expect(comp.adresasCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const objekat: IObjekat = { id: 456 };
      const adresa: IAdresa = { id: 47151 };
      objekat.adresa = adresa;

      activatedRoute.data = of({ objekat });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(objekat));
      expect(comp.adresasCollection).toContain(adresa);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Objekat>>();
      const objekat = { id: 123 };
      jest.spyOn(objekatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objekat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: objekat }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(objekatService.update).toHaveBeenCalledWith(objekat);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Objekat>>();
      const objekat = new Objekat();
      jest.spyOn(objekatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objekat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: objekat }));
      saveSubject.complete();

      // THEN
      expect(objekatService.create).toHaveBeenCalledWith(objekat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Objekat>>();
      const objekat = { id: 123 };
      jest.spyOn(objekatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objekat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(objekatService.update).toHaveBeenCalledWith(objekat);
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
  });
});
