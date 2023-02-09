import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAudio, NewAudio } from '../audio.model';

export type PartialUpdateAudio = Partial<IAudio> & Pick<IAudio, 'idAudio'>;

type RestOf<T extends IAudio | NewAudio> = Omit<T, 'dataCreazione' | 'dataUltimaModifica'> & {
  dataCreazione?: string | null;
  dataUltimaModifica?: string | null;
};

export type RestAudio = RestOf<IAudio>;

export type NewRestAudio = RestOf<NewAudio>;

export type PartialUpdateRestAudio = RestOf<PartialUpdateAudio>;

export type EntityResponseType = HttpResponse<IAudio>;
export type EntityArrayResponseType = HttpResponse<IAudio[]>;

@Injectable({ providedIn: 'root' })
export class AudioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/audio');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(audio: NewAudio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(audio);
    return this.http.post<RestAudio>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(audio: IAudio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(audio);
    return this.http
      .put<RestAudio>(`${this.resourceUrl}/${this.getAudioIdentifier(audio)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(audio: PartialUpdateAudio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(audio);
    return this.http
      .patch<RestAudio>(`${this.resourceUrl}/${this.getAudioIdentifier(audio)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAudio>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAudio[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAudioIdentifier(audio: Pick<IAudio, 'idAudio'>): number {
    return audio.idAudio;
  }

  compareAudio(o1: Pick<IAudio, 'idAudio'> | null, o2: Pick<IAudio, 'idAudio'> | null): boolean {
    return o1 && o2 ? this.getAudioIdentifier(o1) === this.getAudioIdentifier(o2) : o1 === o2;
  }

  addAudioToCollectionIfMissing<Type extends Pick<IAudio, 'idAudio'>>(
    audioCollection: Type[],
    ...audioToCheck: (Type | null | undefined)[]
  ): Type[] {
    const audio: Type[] = audioToCheck.filter(isPresent);
    if (audio.length > 0) {
      const audioCollectionIdentifiers = audioCollection.map(audioItem => this.getAudioIdentifier(audioItem)!);
      const audioToAdd = audio.filter(audioItem => {
        const audioIdentifier = this.getAudioIdentifier(audioItem);
        if (audioCollectionIdentifiers.includes(audioIdentifier)) {
          return false;
        }
        audioCollectionIdentifiers.push(audioIdentifier);
        return true;
      });
      return [...audioToAdd, ...audioCollection];
    }
    return audioCollection;
  }

  protected convertDateFromClient<T extends IAudio | NewAudio | PartialUpdateAudio>(audio: T): RestOf<T> {
    return {
      ...audio,
      dataCreazione: audio.dataCreazione?.toJSON() ?? null,
      dataUltimaModifica: audio.dataUltimaModifica?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAudio: RestAudio): IAudio {
    return {
      ...restAudio,
      dataCreazione: restAudio.dataCreazione ? dayjs(restAudio.dataCreazione) : undefined,
      dataUltimaModifica: restAudio.dataUltimaModifica ? dayjs(restAudio.dataUltimaModifica) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAudio>): HttpResponse<IAudio> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAudio[]>): HttpResponse<IAudio[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
