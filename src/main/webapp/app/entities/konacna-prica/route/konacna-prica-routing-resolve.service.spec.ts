import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IKonacnaPrica, KonacnaPrica } from '../konacna-prica.model';
import { KonacnaPricaService } from '../service/konacna-prica.service';

import { KonacnaPricaRoutingResolveService } from './konacna-prica-routing-resolve.service';

describe('KonacnaPrica routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: KonacnaPricaRoutingResolveService;
  let service: KonacnaPricaService;
  let resultKonacnaPrica: IKonacnaPrica | undefined;

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
    routingResolveService = TestBed.inject(KonacnaPricaRoutingResolveService);
    service = TestBed.inject(KonacnaPricaService);
    resultKonacnaPrica = undefined;
  });

  describe('resolve', () => {
    it('should return IKonacnaPrica returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKonacnaPrica = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultKonacnaPrica).toEqual({ id: 123 });
    });

    it('should return new IKonacnaPrica if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKonacnaPrica = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultKonacnaPrica).toEqual(new KonacnaPrica());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as KonacnaPrica })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKonacnaPrica = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultKonacnaPrica).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
