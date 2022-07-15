import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { KomentarNaPricuService } from '../service/komentar-na-pricu.service';
import { IKomentarNaPricu, KomentarNaPricu } from '../komentar-na-pricu.model';
import { IPrica } from 'app/entities/prica/prica.model';
import { PricaService } from 'app/entities/prica/service/prica.service';

import { KomentarNaPricuUpdateComponent } from './komentar-na-pricu-update.component';

describe('KomentarNaPricu Management Update Component', () => {
  let comp: KomentarNaPricuUpdateComponent;
  let fixture: ComponentFixture<KomentarNaPricuUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let komentarNaPricuService: KomentarNaPricuService;
  let pricaService: PricaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [KomentarNaPricuUpdateComponent],
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
      .overrideTemplate(KomentarNaPricuUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(KomentarNaPricuUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    komentarNaPricuService = TestBed.inject(KomentarNaPricuService);
    pricaService = TestBed.inject(PricaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Prica query and add missing value', () => {
      const komentarNaPricu: IKomentarNaPricu = { id: 456 };
      const prica: IPrica = { id: 8710 };
      komentarNaPricu.prica = prica;

      const pricaCollection: IPrica[] = [{ id: 23623 }];
      jest.spyOn(pricaService, 'query').mockReturnValue(of(new HttpResponse({ body: pricaCollection })));
      const additionalPricas = [prica];
      const expectedCollection: IPrica[] = [...additionalPricas, ...pricaCollection];
      jest.spyOn(pricaService, 'addPricaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ komentarNaPricu });
      comp.ngOnInit();

      expect(pricaService.query).toHaveBeenCalled();
      expect(pricaService.addPricaToCollectionIfMissing).toHaveBeenCalledWith(pricaCollection, ...additionalPricas);
      expect(comp.pricasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const komentarNaPricu: IKomentarNaPricu = { id: 456 };
      const prica: IPrica = { id: 61839 };
      komentarNaPricu.prica = prica;

      activatedRoute.data = of({ komentarNaPricu });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(komentarNaPricu));
      expect(comp.pricasSharedCollection).toContain(prica);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<KomentarNaPricu>>();
      const komentarNaPricu = { id: 123 };
      jest.spyOn(komentarNaPricuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ komentarNaPricu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: komentarNaPricu }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(komentarNaPricuService.update).toHaveBeenCalledWith(komentarNaPricu);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<KomentarNaPricu>>();
      const komentarNaPricu = new KomentarNaPricu();
      jest.spyOn(komentarNaPricuService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ komentarNaPricu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: komentarNaPricu }));
      saveSubject.complete();

      // THEN
      expect(komentarNaPricuService.create).toHaveBeenCalledWith(komentarNaPricu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<KomentarNaPricu>>();
      const komentarNaPricu = { id: 123 };
      jest.spyOn(komentarNaPricuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ komentarNaPricu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(komentarNaPricuService.update).toHaveBeenCalledWith(komentarNaPricu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPricaById', () => {
      it('Should return tracked Prica primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPricaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
