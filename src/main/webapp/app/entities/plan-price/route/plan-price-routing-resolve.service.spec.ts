import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPlanPrice, PlanPrice } from '../plan-price.model';
import { PlanPriceService } from '../service/plan-price.service';

import { PlanPriceRoutingResolveService } from './plan-price-routing-resolve.service';

describe('PlanPrice routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PlanPriceRoutingResolveService;
  let service: PlanPriceService;
  let resultPlanPrice: IPlanPrice | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(PlanPriceRoutingResolveService);
    service = TestBed.inject(PlanPriceService);
    resultPlanPrice = undefined;
  });

  describe('resolve', () => {
    it('should return IPlanPrice returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlanPrice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPlanPrice).toEqual({ id: 123 });
    });

    it('should return new IPlanPrice if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlanPrice = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPlanPrice).toEqual(new PlanPrice());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PlanPrice })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlanPrice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPlanPrice).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
