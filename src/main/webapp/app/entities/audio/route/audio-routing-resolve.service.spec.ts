import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IAudio } from '../audio.model';
import { AudioService } from '../service/audio.service';

import { AudioRoutingResolveService } from './audio-routing-resolve.service';

describe('Audio routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AudioRoutingResolveService;
  let service: AudioService;
  let resultAudio: IAudio | null | undefined;

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
    routingResolveService = TestBed.inject(AudioRoutingResolveService);
    service = TestBed.inject(AudioService);
    resultAudio = undefined;
  });

  describe('resolve', () => {
    it('should return IAudio returned by find', () => {
      // GIVEN
      service.find = jest.fn(idAudio => of(new HttpResponse({ body: { idAudio } })));
      mockActivatedRouteSnapshot.params = { idAudio: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAudio = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAudio).toEqual({ idAudio: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAudio = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAudio).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IAudio>({ body: null })));
      mockActivatedRouteSnapshot.params = { idAudio: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAudio = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAudio).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
