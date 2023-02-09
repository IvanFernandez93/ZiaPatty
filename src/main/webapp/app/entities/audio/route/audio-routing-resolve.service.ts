import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAudio } from '../audio.model';
import { AudioService } from '../service/audio.service';

@Injectable({ providedIn: 'root' })
export class AudioRoutingResolveService implements Resolve<IAudio | null> {
  constructor(protected service: AudioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAudio | null | never> {
    const id = route.params['idAudio'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((audio: HttpResponse<IAudio>) => {
          if (audio.body) {
            return of(audio.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
