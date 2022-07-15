import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { INeDolasci, NeDolasci } from '../ne-dolasci.model';
import { NeDolasciService } from '../service/ne-dolasci.service';

import { NeDolasciRoutingResolveService } from './ne-dolasci-routing-resolve.service';

describe('NeDolasci routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: NeDolasciRoutingResolveService;
  let service: NeDolasciService;
  let resultNeDolasci: INeDolasci | undefined;

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
    routingResolveService = TestBed.inject(NeDolasciRoutingResolveService);
    service = TestBed.inject(NeDolasciService);
    resultNeDolasci = undefined;
  });

  describe('resolve', () => {
    it('should return INeDolasci returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNeDolasci = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNeDolasci).toEqual({ id: 123 });
    });

    it('should return new INeDolasci if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNeDolasci = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultNeDolasci).toEqual(new NeDolasci());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as NeDolasci })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNeDolasci = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNeDolasci).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
