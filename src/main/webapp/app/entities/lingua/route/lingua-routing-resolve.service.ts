import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILingua } from '../lingua.model';
import { LinguaService } from '../service/lingua.service';

@Injectable({ providedIn: 'root' })
export class LinguaRoutingResolveService implements Resolve<ILingua | null> {
  constructor(protected service: LinguaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILingua | null | never> {
    const id = route.params['idLingua'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lingua: HttpResponse<ILingua>) => {
          if (lingua.body) {
            return of(lingua.body);
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
