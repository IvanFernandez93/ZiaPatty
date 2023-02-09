import dayjs from 'dayjs/esm';

import { Stato } from 'app/entities/enumerations/stato.model';

import { IAudio, NewAudio } from './audio.model';

export const sampleWithRequiredData: IAudio = {
  idAudio: 5140,
};

export const sampleWithPartialData: IAudio = {
  idAudio: 11330,
};

export const sampleWithFullData: IAudio = {
  idAudio: 88992,
  nome: 'synthesize',
  codiceStato: Stato['BOZZA'],
  track: '../fake-data/blob/hipster.png',
  trackContentType: 'unknown',
  dataCreazione: dayjs('2023-02-08T03:39'),
  dataUltimaModifica: dayjs('2023-02-08T11:56'),
  eliminato: false,
};

export const sampleWithNewData: NewAudio = {
  idAudio: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
