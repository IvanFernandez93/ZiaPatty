import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFraseAudio, NewFraseAudio } from '../frase-audio.model';

export type PartialUpdateFraseAudio = Partial<IFraseAudio> & Pick<IFraseAudio, 'id'>;

export type EntityResponseType = HttpResponse<IFraseAudio>;
export type EntityArrayResponseType = HttpResponse<IFraseAudio[]>;

@Injectable({ providedIn: 'root' })
export class FraseAudioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/frase-audios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fraseAudio: NewFraseAudio): Observable<EntityResponseType> {
    return this.http.post<IFraseAudio>(this.resourceUrl, fraseAudio, { observe: 'response' });
  }

  update(fraseAudio: IFraseAudio): Observable<EntityResponseType> {
    return this.http.put<IFraseAudio>(`${this.resourceUrl}/${this.getFraseAudioIdentifier(fraseAudio)}`, fraseAudio, {
      observe: 'response',
    });
  }

  partialUpdate(fraseAudio: PartialUpdateFraseAudio): Observable<EntityResponseType> {
    return this.http.patch<IFraseAudio>(`${this.resourceUrl}/${this.getFraseAudioIdentifier(fraseAudio)}`, fraseAudio, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFraseAudio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFraseAudio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFraseAudioIdentifier(fraseAudio: Pick<IFraseAudio, 'id'>): number {
    return fraseAudio.id;
  }

  compareFraseAudio(o1: Pick<IFraseAudio, 'id'> | null, o2: Pick<IFraseAudio, 'id'> | null): boolean {
    return o1 && o2 ? this.getFraseAudioIdentifier(o1) === this.getFraseAudioIdentifier(o2) : o1 === o2;
  }

  addFraseAudioToCollectionIfMissing<Type extends Pick<IFraseAudio, 'id'>>(
    fraseAudioCollection: Type[],
    ...fraseAudiosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fraseAudios: Type[] = fraseAudiosToCheck.filter(isPresent);
    if (fraseAudios.length > 0) {
      const fraseAudioCollectionIdentifiers = fraseAudioCollection.map(fraseAudioItem => this.getFraseAudioIdentifier(fraseAudioItem)!);
      const fraseAudiosToAdd = fraseAudios.filter(fraseAudioItem => {
        const fraseAudioIdentifier = this.getFraseAudioIdentifier(fraseAudioItem);
        if (fraseAudioCollectionIdentifiers.includes(fraseAudioIdentifier)) {
          return false;
        }
        fraseAudioCollectionIdentifiers.push(fraseAudioIdentifier);
        return true;
      });
      return [...fraseAudiosToAdd, ...fraseAudioCollection];
    }
    return fraseAudioCollection;
  }
}
