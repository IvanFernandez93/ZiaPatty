import dayjs from 'dayjs/esm';

import { Stato } from 'app/entities/enumerations/stato.model';

import { ICategoria, NewCategoria } from './categoria.model';

export const sampleWithRequiredData: ICategoria = {
  idCategoria: 34642,
};

export const sampleWithPartialData: ICategoria = {
  idCategoria: 41627,
  idCategoriaPadre: 94488,
};

export const sampleWithFullData: ICategoria = {
  idCategoria: 33522,
  nome: 'withdrawal',
  idCategoriaPadre: 65789,
  codiceStato: Stato['BOZZA'],
  dataCreazione: dayjs('2023-02-08T11:05'),
  dataUltimaModifica: dayjs('2023-02-08T00:57'),
  eliminato: false,
};

export const sampleWithNewData: NewCategoria = {
  idCategoria: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
