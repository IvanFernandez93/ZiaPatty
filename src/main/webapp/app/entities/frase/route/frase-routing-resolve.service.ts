import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFrase } from '../frase.model';
import { FraseService } from '../service/frase.service';

@Injectable({ providedIn: 'root' })
export class FraseRoutingResolveService implements Resolve<IFrase | null> {
  constructor(protected service: FraseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFrase | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((frase: HttpResponse<IFrase>) => {
          if (frase.body) {
            return of(frase.body);
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
