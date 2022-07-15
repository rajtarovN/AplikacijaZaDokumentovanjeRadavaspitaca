import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IZapazanjeUVeziDeteta, ZapazanjeUVeziDeteta } from '../zapazanje-u-vezi-deteta.model';
import { ZapazanjeUVeziDetetaService } from '../service/zapazanje-u-vezi-deteta.service';

import { ZapazanjeUVeziDetetaRoutingResolveService } from './zapazanje-u-vezi-deteta-routing-resolve.service';

describe('ZapazanjeUVeziDeteta routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ZapazanjeUVeziDetetaRoutingResolveService;
  let service: ZapazanjeUVeziDetetaService;
  let resultZapazanjeUVeziDeteta: IZapazanjeUVeziDeteta | undefined;

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
    routingResolveService = TestBed.inject(ZapazanjeUVeziDetetaRoutingResolveService);
    service = TestBed.inject(ZapazanjeUVeziDetetaService);
    resultZapazanjeUVeziDeteta = undefined;
  });

  describe('resolve', () => {
    it('should return IZapazanjeUVeziDeteta returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultZapazanjeUVeziDeteta = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultZapazanjeUVeziDeteta).toEqual({ id: 123 });
    });

    it('should return new IZapazanjeUVeziDeteta if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultZapazanjeUVeziDeteta = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultZapazanjeUVeziDeteta).toEqual(new ZapazanjeUVeziDeteta());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ZapazanjeUVeziDeteta })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultZapazanjeUVeziDeteta = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultZapazanjeUVeziDeteta).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
