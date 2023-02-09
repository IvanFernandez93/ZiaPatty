import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ILingua } from '../lingua.model';
import { LinguaService } from '../service/lingua.service';

import { LinguaRoutingResolveService } from './lingua-routing-resolve.service';

describe('Lingua routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LinguaRoutingResolveService;
  let service: LinguaService;
  let resultLingua: ILingua | null | undefined;

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
    routingResolveService = TestBed.inject(LinguaRoutingResolveService);
    service = TestBed.inject(LinguaService);
    resultLingua = undefined;
  });

  describe('resolve', () => {
    it('should return ILingua returned by find', () => {
      // GIVEN
      service.find = jest.fn(idLingua => of(new HttpResponse({ body: { idLingua } })));
      mockActivatedRouteSnapshot.params = { idLingua: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLingua = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLingua).toEqual({ idLingua: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLingua = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLingua).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ILingua>({ body: null })));
      mockActivatedRouteSnapshot.params = { idLingua: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLingua = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLingua).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
