import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IVaspitac, Vaspitac } from '../vaspitac.model';
import { VaspitacService } from '../service/vaspitac.service';

import { VaspitacRoutingResolveService } from './vaspitac-routing-resolve.service';

describe('Vaspitac routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VaspitacRoutingResolveService;
  let service: VaspitacService;
  let resultVaspitac: IVaspitac | undefined;

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
    routingResolveService = TestBed.inject(VaspitacRoutingResolveService);
    service = TestBed.inject(VaspitacService);
    resultVaspitac = undefined;
  });

  describe('resolve', () => {
    it('should return IVaspitac returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVaspitac = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVaspitac).toEqual({ id: 123 });
    });

    it('should return new IVaspitac if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVaspitac = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVaspitac).toEqual(new Vaspitac());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Vaspitac })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVaspitac = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVaspitac).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
