import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategoriaFrase } from '../categoria-frase.model';
import { CategoriaFraseService } from '../service/categoria-frase.service';

@Injectable({ providedIn: 'root' })
export class CategoriaFraseRoutingResolveService implements Resolve<ICategoriaFrase | null> {
  constructor(protected service: CategoriaFraseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICategoriaFrase | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((categoriaFrase: HttpResponse<ICategoriaFrase>) => {
          if (categoriaFrase.body) {
            return of(categoriaFrase.body);
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
