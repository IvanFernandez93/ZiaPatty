import { ICategoriaFrase, NewCategoriaFrase } from './categoria-frase.model';

export const sampleWithRequiredData: ICategoriaFrase = {
  id: 67342,
};

export const sampleWithPartialData: ICategoriaFrase = {
  id: 91544,
  idCategoria: 5402,
};

export const sampleWithFullData: ICategoriaFrase = {
  id: 76676,
  idCategoria: 41083,
  idFrase: 39980,
};

export const sampleWithNewData: NewCategoriaFrase = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
