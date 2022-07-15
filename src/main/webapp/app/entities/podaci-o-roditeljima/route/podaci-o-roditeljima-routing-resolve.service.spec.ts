import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPodaciORoditeljima, PodaciORoditeljima } from '../podaci-o-roditeljima.model';
import { PodaciORoditeljimaService } from '../service/podaci-o-roditeljima.service';

import { PodaciORoditeljimaRoutingResolveService } from './podaci-o-roditeljima-routing-resolve.service';

describe('PodaciORoditeljima routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PodaciORoditeljimaRoutingResolveService;
  let service: PodaciORoditeljimaService;
  let resultPodaciORoditeljima: IPodaciORoditeljima | undefined;

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
    routingResolveService = TestBed.inject(PodaciORoditeljimaRoutingResolveService);
    service = TestBed.inject(PodaciORoditeljimaService);
    resultPodaciORoditeljima = undefined;
  });

  describe('resolve', () => {
    it('should return IPodaciORoditeljima returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPodaciORoditeljima = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPodaciORoditeljima).toEqual({ id: 123 });
    });

    it('should return new IPodaciORoditeljima if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPodaciORoditeljima = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPodaciORoditeljima).toEqual(new PodaciORoditeljima());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PodaciORoditeljima })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPodaciORoditeljima = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPodaciORoditeljima).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
