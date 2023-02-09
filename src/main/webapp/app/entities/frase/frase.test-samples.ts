import dayjs from 'dayjs/esm';

import { Stato } from 'app/entities/enumerations/stato.model';

import { IFrase, NewFrase } from './frase.model';

export const sampleWithRequiredData: IFrase = {
  id: 77332,
};

export const sampleWithPartialData: IFrase = {
  id: 93308,
  codiceStato: Stato['BOZZA'],
  eliminato: true,
};

export const sampleWithFullData: IFrase = {
  id: 66724,
  idFrase: 74721,
  frase: 'Books system',
  codiceStato: Stato['PUBBLICATO'],
  dataCreazione: dayjs('2023-02-08T22:32'),
  dataUltimaModifica: dayjs('2023-02-08T19:36'),
  eliminato: false,
};

export const sampleWithNewData: NewFrase = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
