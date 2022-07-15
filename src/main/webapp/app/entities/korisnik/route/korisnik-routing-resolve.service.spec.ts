import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IKorisnik, Korisnik } from '../korisnik.model';
import { KorisnikService } from '../service/korisnik.service';

import { KorisnikRoutingResolveService } from './korisnik-routing-resolve.service';

describe('Korisnik routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: KorisnikRoutingResolveService;
  let service: KorisnikService;
  let resultKorisnik: IKorisnik | undefined;

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
    routingResolveService = TestBed.inject(KorisnikRoutingResolveService);
    service = TestBed.inject(KorisnikService);
    resultKorisnik = undefined;
  });

  describe('resolve', () => {
    it('should return IKorisnik returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKorisnik = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultKorisnik).toEqual({ id: 123 });
    });

    it('should return new IKorisnik if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKorisnik = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultKorisnik).toEqual(new Korisnik());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Korisnik })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultKorisnik = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultKorisnik).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
