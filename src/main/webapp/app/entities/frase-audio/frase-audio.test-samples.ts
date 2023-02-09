import { IFraseAudio, NewFraseAudio } from './frase-audio.model';

export const sampleWithRequiredData: IFraseAudio = {
  id: 55018,
};

export const sampleWithPartialData: IFraseAudio = {
  id: 99863,
  idCategoria: 53099,
  idFrase: 58743,
};

export const sampleWithFullData: IFraseAudio = {
  id: 71278,
  idCategoria: 65653,
  idFrase: 53841,
};

export const sampleWithNewData: NewFraseAudio = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
