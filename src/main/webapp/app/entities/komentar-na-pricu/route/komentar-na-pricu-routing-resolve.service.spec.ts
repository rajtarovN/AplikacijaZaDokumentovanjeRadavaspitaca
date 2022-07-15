import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IKomentarNaPricu, KomentarNaPricu } from '../komentar-na-pricu.model';
import { KomentarNaPricuService } from '../service/komentar-na-pricu.service';

import { KomentarNaPricuRoutingResolveService } from './komentar-na-pricu-routing-resolve.service';

describe('KomentarNaPricu routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: KomentarNaPricuRoutingResolveService;
  let service: KomentarNaPricuService;
  let resultKomentarNaPricu: IKomentarNaPricu | undefined;

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
    routingResolveService = TestBed.inject(KomentarNaPricuRoutingResolveService);
    service = TestBed.inject(KomentarNaPricuService);
    resultKomentarNaPricu = undefined;
  });

  describe('resolve', () => {
    it('should return IKomentarNaPricu returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKomentarNaPricu = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultKomentarNaPricu).toEqual({ id: 123 });
    });

    it('should return new IKomentarNaPricu if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKomentarNaPricu = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultKomentarNaPricu).toEqual(new KomentarNaPricu());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as KomentarNaPricu })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKomentarNaPricu = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultKomentarNaPricu).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
