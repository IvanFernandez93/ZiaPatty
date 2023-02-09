import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFrase, NewFrase } from '../frase.model';

export type PartialUpdateFrase = Partial<IFrase> & Pick<IFrase, 'id'>;

type RestOf<T extends IFrase | NewFrase> = Omit<T, 'dataCreazione' | 'dataUltimaModifica'> & {
  dataCreazione?: string | null;
  dataUltimaModifica?: string | null;
};

export type RestFrase = RestOf<IFrase>;

export type NewRestFrase = RestOf<NewFrase>;

export type PartialUpdateRestFrase = RestOf<PartialUpdateFrase>;

export type EntityResponseType = HttpResponse<IFrase>;
export type EntityArrayResponseType = HttpResponse<IFrase[]>;

@Injectable({ providedIn: 'root' })
export class FraseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/frases');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(frase: NewFrase): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frase);
    return this.http.post<RestFrase>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(frase: IFrase): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frase);
    return this.http
      .put<RestFrase>(`${this.resourceUrl}/${this.getFraseIdentifier(frase)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(frase: PartialUpdateFrase): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frase);
    return this.http
      .patch<RestFrase>(`${this.resourceUrl}/${this.getFraseIdentifier(frase)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFrase>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFrase[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFraseIdentifier(frase: Pick<IFrase, 'id'>): number {
    return frase.id;
  }

  compareFrase(o1: Pick<IFrase, 'id'> | null, o2: Pick<IFrase, 'id'> | null): boolean {
    return o1 && o2 ? this.getFraseIdentifier(o1) === this.getFraseIdentifier(o2) : o1 === o2;
  }

  addFraseToCollectionIfMissing<Type extends Pick<IFrase, 'id'>>(
    fraseCollection: Type[],
    ...frasesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const frases: Type[] = frasesToCheck.filter(isPresent);
    if (frases.length > 0) {
      const fraseCollectionIdentifiers = fraseCollection.map(fraseItem => this.getFraseIdentifier(fraseItem)!);
      const frasesToAdd = frases.filter(fraseItem => {
        const fraseIdentifier = this.getFraseIdentifier(fraseItem);
        if (fraseCollectionIdentifiers.includes(fraseIdentifier)) {
          return false;
        }
        fraseCollectionIdentifiers.push(fraseIdentifier);
        return true;
      });
      return [...frasesToAdd, ...fraseCollection];
    }
    return fraseCollection;
  }

  protected convertDateFromClient<T extends IFrase | NewFrase | PartialUpdateFrase>(frase: T): RestOf<T> {
    return {
      ...frase,
      dataCreazione: frase.dataCreazione?.toJSON() ?? null,
      dataUltimaModifica: frase.dataUltimaModifica?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFrase: RestFrase): IFrase {
    return {
      ...restFrase,
      dataCreazione: restFrase.dataCreazione ? dayjs(restFrase.dataCreazione) : undefined,
      dataUltimaModifica: restFrase.dataUltimaModifica ? dayjs(restFrase.dataUltimaModifica) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFrase>): HttpResponse<IFrase> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFrase[]>): HttpResponse<IFrase[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
