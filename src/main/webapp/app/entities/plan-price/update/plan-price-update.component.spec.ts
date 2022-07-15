import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PlanPriceService } from '../service/plan-price.service';
import { IPlanPrice, PlanPrice } from '../plan-price.model';

import { PlanPriceUpdateComponent } from './plan-price-update.component';

describe('PlanPrice Management Update Component', () => {
  let comp: PlanPriceUpdateComponent;
  let fixture: ComponentFixture<PlanPriceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let planPriceService: PlanPriceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PlanPriceUpdateComponent],
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
      .overrideTemplate(PlanPriceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlanPriceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    planPriceService = TestBed.inject(PlanPriceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const planPrice: IPlanPrice = { id: 456 };

      activatedRoute.data = of({ planPrice });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(planPrice));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PlanPrice>>();
      const planPrice = { id: 123 };
      jest.spyOn(planPriceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planPrice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: planPrice }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(planPriceService.update).toHaveBeenCalledWith(planPrice);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PlanPrice>>();
      const planPrice = new PlanPrice();
      jest.spyOn(planPriceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planPrice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: planPrice }));
      saveSubject.complete();

      // THEN
      expect(planPriceService.create).toHaveBeenCalledWith(planPrice);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PlanPrice>>();
      const planPrice = { id: 123 };
      jest.spyOn(planPriceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planPrice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(planPriceService.update).toHaveBeenCalledWith(planPrice);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
