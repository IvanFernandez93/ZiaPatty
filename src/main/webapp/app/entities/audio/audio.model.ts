import dayjs from 'dayjs/esm';
import { Stato } from 'app/entities/enumerations/stato.model';

export interface IAudio {
  idAudio: number;
  nome?: string | null;
  codiceStato?: Stato | null;
  track?: string | null;
  trackContentType?: string | null;
  dataCreazione?: dayjs.Dayjs | null;
  dataUltimaModifica?: dayjs.Dayjs | null;
  eliminato?: boolean | null;
}

export type NewAudio = Omit<IAudio, 'idAudio'> & { idAudio: null };
