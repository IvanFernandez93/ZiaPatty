import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategoriaFrase, NewCategoriaFrase } from '../categoria-frase.model';

export type PartialUpdateCategoriaFrase = Partial<ICategoriaFrase> & Pick<ICategoriaFrase, 'id'>;

export type EntityResponseType = HttpResponse<ICategoriaFrase>;
export type EntityArrayResponseType = HttpResponse<ICategoriaFrase[]>;

@Injectable({ providedIn: 'root' })
export class CategoriaFraseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/categoria-frases');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(categoriaFrase: NewCategoriaFrase): Observable<EntityResponseType> {
    return this.http.post<ICategoriaFrase>(this.resourceUrl, categoriaFrase, { observe: 'response' });
  }

  update(categoriaFrase: ICategoriaFrase): Observable<EntityResponseType> {
    return this.http.put<ICategoriaFrase>(`${this.resourceUrl}/${this.getCategoriaFraseIdentifier(categoriaFrase)}`, categoriaFrase, {
      observe: 'response',
    });
  }

  partialUpdate(categoriaFrase: PartialUpdateCategoriaFrase): Observable<EntityResponseType> {
    return this.http.patch<ICategoriaFrase>(`${this.resourceUrl}/${this.getCategoriaFraseIdentifier(categoriaFrase)}`, categoriaFrase, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICategoriaFrase>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategoriaFrase[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCategoriaFraseIdentifier(categoriaFrase: Pick<ICategoriaFrase, 'id'>): number {
    return categoriaFrase.id;
  }

  compareCategoriaFrase(o1: Pick<ICategoriaFrase, 'id'> | null, o2: Pick<ICategoriaFrase, 'id'> | null): boolean {
    return o1 && o2 ? this.getCategoriaFraseIdentifier(o1) === this.getCategoriaFraseIdentifier(o2) : o1 === o2;
  }

  addCategoriaFraseToCollectionIfMissing<Type extends Pick<ICategoriaFrase, 'id'>>(
    categoriaFraseCollection: Type[],
    ...categoriaFrasesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const categoriaFrases: Type[] = categoriaFrasesToCheck.filter(isPresent);
    if (categoriaFrases.length > 0) {
      const categoriaFraseCollectionIdentifiers = categoriaFraseCollection.map(
        categoriaFraseItem => this.getCategoriaFraseIdentifier(categoriaFraseItem)!
      );
      const categoriaFrasesToAdd = categoriaFrases.filter(categoriaFraseItem => {
        const categoriaFraseIdentifier = this.getCategoriaFraseIdentifier(categoriaFraseItem);
        if (categoriaFraseCollectionIdentifiers.includes(categoriaFraseIdentifier)) {
          return false;
        }
        categoriaFraseCollectionIdentifiers.push(categoriaFraseIdentifier);
        return true;
      });
      return [...categoriaFrasesToAdd, ...categoriaFraseCollection];
    }
    return categoriaFraseCollection;
  }
}
