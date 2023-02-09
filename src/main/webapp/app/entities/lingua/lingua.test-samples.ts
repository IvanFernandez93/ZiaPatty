import dayjs from 'dayjs/esm';

import { ILingua, NewLingua } from './lingua.model';

export const sampleWithRequiredData: ILingua = {
  idLingua: 5831,
};

export const sampleWithPartialData: ILingua = {
  idLingua: 91777,
  codiceLingua: 'Architect Korea',
  nomeLingua: 'Plastic',
  dataCreazione: dayjs('2023-02-08T07:18'),
};

export const sampleWithFullData: ILingua = {
  idLingua: 86316,
  codiceLingua: 'infomediaries leading',
  nomeLingua: 'reboot',
  dataCreazione: dayjs('2023-02-08T12:46'),
  dataUltimaModifica: dayjs('2023-02-08T09:46'),
  eliminato: false,
};

export const sampleWithNewData: NewLingua = {
  idLingua: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
