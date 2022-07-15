import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PricaService } from '../service/prica.service';
import { IPrica, Prica } from '../prica.model';
import { IPlanPrice } from 'app/entities/plan-price/plan-price.model';
import { PlanPriceService } from 'app/entities/plan-price/service/plan-price.service';
import { IKonacnaPrica } from 'app/entities/konacna-prica/konacna-prica.model';
import { KonacnaPricaService } from 'app/entities/konacna-prica/service/konacna-prica.service';
import { IDnevnik } from 'app/entities/dnevnik/dnevnik.model';
import { DnevnikService } from 'app/entities/dnevnik/service/dnevnik.service';

import { PricaUpdateComponent } from './prica-update.component';

describe('Prica Management Update Component', () => {
  let comp: PricaUpdateComponent;
  let fixture: ComponentFixture<PricaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pricaService: PricaService;
  let planPriceService: PlanPriceService;
  let konacnaPricaService: KonacnaPricaService;
  let dnevnikService: DnevnikService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PricaUpdateComponent],
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
      .overrideTemplate(PricaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PricaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pricaService = TestBed.inject(PricaService);
    planPriceService = TestBed.inject(PlanPriceService);
    konacnaPricaService = TestBed.inject(KonacnaPricaService);
    dnevnikService = TestBed.inject(DnevnikService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call planPrice query and add missing value', () => {
      const prica: IPrica = { id: 456 };
      const planPrice: IPlanPrice = { id: 25233 };
      prica.planPrice = planPrice;

      const planPriceCollection: IPlanPrice[] = [{ id: 43279 }];
      jest.spyOn(planPriceService, 'query').mockReturnValue(of(new HttpResponse({ body: planPriceCollection })));
      const expectedCollection: IPlanPrice[] = [planPrice, ...planPriceCollection];
      jest.spyOn(planPriceService, 'addPlanPriceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prica });
      comp.ngOnInit();

      expect(planPriceService.query).toHaveBeenCalled();
      expect(planPriceService.addPlanPriceToCollectionIfMissing).toHaveBeenCalledWith(planPriceCollection, planPrice);
      expect(comp.planPricesCollection).toEqual(expectedCollection);
    });

    it('Should call konacnaPrica query and add missing value', () => {
      const prica: IPrica = { id: 456 };
      const konacnaPrica: IKonacnaPrica = { id: 45503 };
      prica.konacnaPrica = konacnaPrica;

      const konacnaPricaCollection: IKonacnaPrica[] = [{ id: 13530 }];
      jest.spyOn(konacnaPricaService, 'query').mockReturnValue(of(new HttpResponse({ body: konacnaPricaCollection })));
      const expectedCollection: IKonacnaPrica[] = [konacnaPrica, ...konacnaPricaCollection];
      jest.spyOn(konacnaPricaService, 'addKonacnaPricaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prica });
      comp.ngOnInit();

      expect(konacnaPricaService.query).toHaveBeenCalled();
      expect(konacnaPricaService.addKonacnaPricaToCollectionIfMissing).toHaveBeenCalledWith(konacnaPricaCollection, konacnaPrica);
      expect(comp.konacnaPricasCollection).toEqual(expectedCollection);
    });

    it('Should call Dnevnik query and add missing value', () => {
      const prica: IPrica = { id: 456 };
      const dnevnik: IDnevnik = { id: 2279 };
      prica.dnevnik = dnevnik;

      const dnevnikCollection: IDnevnik[] = [{ id: 83168 }];
      jest.spyOn(dnevnikService, 'query').mockReturnValue(of(new HttpResponse({ body: dnevnikCollection })));
      const additionalDnevniks = [dnevnik];
      const expectedCollection: IDnevnik[] = [...additionalDnevniks, ...dnevnikCollection];
      jest.spyOn(dnevnikService, 'addDnevnikToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prica });
      comp.ngOnInit();

      expect(dnevnikService.query).toHaveBeenCalled();
      expect(dnevnikService.addDnevnikToCollectionIfMissing).toHaveBeenCalledWith(dnevnikCollection, ...additionalDnevniks);
      expect(comp.dnevniksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prica: IPrica = { id: 456 };
      const planPrice: IPlanPrice = { id: 55188 };
      prica.planPrice = planPrice;
      const konacnaPrica: IKonacnaPrica = { id: 35189 };
      prica.konacnaPrica = konacnaPrica;
      const dnevnik: IDnevnik = { id: 14738 };
      prica.dnevnik = dnevnik;

      activatedRoute.data = of({ prica });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(prica));
      expect(comp.planPricesCollection).toContain(planPrice);
      expect(comp.konacnaPricasCollection).toContain(konacnaPrica);
      expect(comp.dnevniksSharedCollection).toContain(dnevnik);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Prica>>();
      const prica = { id: 123 };
      jest.spyOn(pricaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prica }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(pricaService.update).toHaveBeenCalledWith(prica);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Prica>>();
      const prica = new Prica();
      jest.spyOn(pricaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prica }));
      saveSubject.complete();

      // THEN
      expect(pricaService.create).toHaveBeenCalledWith(prica);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Prica>>();
      const prica = { id: 123 };
      jest.spyOn(pricaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pricaService.update).toHaveBeenCalledWith(prica);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPlanPriceById', () => {
      it('Should return tracked PlanPrice primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlanPriceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackKonacnaPricaById', () => {
      it('Should return tracked KonacnaPrica primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackKonacnaPricaById(0, entity);
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
