import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILingua, NewLingua } from '../lingua.model';

export type PartialUpdateLingua = Partial<ILingua> & Pick<ILingua, 'idLingua'>;

type RestOf<T extends ILingua | NewLingua> = Omit<T, 'dataCreazione' | 'dataUltimaModifica'> & {
  dataCreazione?: string | null;
  dataUltimaModifica?: string | null;
};

export type RestLingua = RestOf<ILingua>;

export type NewRestLingua = RestOf<NewLingua>;

export type PartialUpdateRestLingua = RestOf<PartialUpdateLingua>;

export type EntityResponseType = HttpResponse<ILingua>;
export type EntityArrayResponseType = HttpResponse<ILingua[]>;

@Injectable({ providedIn: 'root' })
export class LinguaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/linguas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lingua: NewLingua): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lingua);
    return this.http
      .post<RestLingua>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(lingua: ILingua): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lingua);
    return this.http
      .put<RestLingua>(`${this.resourceUrl}/${this.getLinguaIdentifier(lingua)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(lingua: PartialUpdateLingua): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lingua);
    return this.http
      .patch<RestLingua>(`${this.resourceUrl}/${this.getLinguaIdentifier(lingua)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLingua>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLingua[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLinguaIdentifier(lingua: Pick<ILingua, 'idLingua'>): number {
    return lingua.idLingua;
  }

  compareLingua(o1: Pick<ILingua, 'idLingua'> | null, o2: Pick<ILingua, 'idLingua'> | null): boolean {
    return o1 && o2 ? this.getLinguaIdentifier(o1) === this.getLinguaIdentifier(o2) : o1 === o2;
  }

  addLinguaToCollectionIfMissing<Type extends Pick<ILingua, 'idLingua'>>(
    linguaCollection: Type[],
    ...linguasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const linguas: Type[] = linguasToCheck.filter(isPresent);
    if (linguas.length > 0) {
      const linguaCollectionIdentifiers = linguaCollection.map(linguaItem => this.getLinguaIdentifier(linguaItem)!);
      const linguasToAdd = linguas.filter(linguaItem => {
        const linguaIdentifier = this.getLinguaIdentifier(linguaItem);
        if (linguaCollectionIdentifiers.includes(linguaIdentifier)) {
          return false;
        }
        linguaCollectionIdentifiers.push(linguaIdentifier);
        return true;
      });
      return [...linguasToAdd, ...linguaCollection];
    }
    return linguaCollection;
  }

  protected convertDateFromClient<T extends ILingua | NewLingua | PartialUpdateLingua>(lingua: T): RestOf<T> {
    return {
      ...lingua,
      dataCreazione: lingua.dataCreazione?.toJSON() ?? null,
      dataUltimaModifica: lingua.dataUltimaModifica?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restLingua: RestLingua): ILingua {
    return {
      ...restLingua,
      dataCreazione: restLingua.dataCreazione ? dayjs(restLingua.dataCreazione) : undefined,
      dataUltimaModifica: restLingua.dataUltimaModifica ? dayjs(restLingua.dataUltimaModifica) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLingua>): HttpResponse<ILingua> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLingua[]>): HttpResponse<ILingua[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
