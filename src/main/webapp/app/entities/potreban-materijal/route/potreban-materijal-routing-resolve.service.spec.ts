import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPotrebanMaterijal, PotrebanMaterijal } from '../potreban-materijal.model';
import { PotrebanMaterijalService } from '../service/potreban-materijal.service';

import { PotrebanMaterijalRoutingResolveService } from './potreban-materijal-routing-resolve.service';

describe('PotrebanMaterijal routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PotrebanMaterijalRoutingResolveService;
  let service: PotrebanMaterijalService;
  let resultPotrebanMaterijal: IPotrebanMaterijal | undefined;

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
    routingResolveService = TestBed.inject(PotrebanMaterijalRoutingResolveService);
    service = TestBed.inject(PotrebanMaterijalService);
    resultPotrebanMaterijal = undefined;
  });

  describe('resolve', () => {
    it('should return IPotrebanMaterijal returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPotrebanMaterijal = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPotrebanMaterijal).toEqual({ id: 123 });
    });

    it('should return new IPotrebanMaterijal if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPotrebanMaterijal = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPotrebanMaterijal).toEqual(new PotrebanMaterijal());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PotrebanMaterijal })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPotrebanMaterijal = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPotrebanMaterijal).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
