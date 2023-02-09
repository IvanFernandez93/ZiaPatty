import { IFrase } from 'app/entities/frase/frase.model';
import { IAudio } from 'app/entities/audio/audio.model';

export interface IFraseAudio {
  id: number;
  idCategoria?: number | null;
  idFrase?: number | null;
  frase?: Pick<IFrase, 'id'> | null;
  audio?: Pick<IAudio, 'idAudio'> | null;
}

export type NewFraseAudio = Omit<IFraseAudio, 'id'> & { id: null };
