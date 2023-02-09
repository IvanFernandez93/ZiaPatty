import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFraseAudio } from '../frase-audio.model';
import { FraseAudioService } from '../service/frase-audio.service';

@Injectable({ providedIn: 'root' })
export class FraseAudioRoutingResolveService implements Resolve<IFraseAudio | null> {
  constructor(protected service: FraseAudioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFraseAudio | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fraseAudio: HttpResponse<IFraseAudio>) => {
          if (fraseAudio.body) {
            return of(fraseAudio.body);
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
