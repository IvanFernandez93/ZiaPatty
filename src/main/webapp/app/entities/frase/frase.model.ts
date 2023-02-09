import dayjs from 'dayjs/esm';
import { ILingua } from 'app/entities/lingua/lingua.model';
import { Stato } from 'app/entities/enumerations/stato.model';

export interface IFrase {
  id: number;
  idFrase?: number | null;
  frase?: string | null;
  codiceStato?: Stato | null;
  dataCreazione?: dayjs.Dayjs | null;
  dataUltimaModifica?: dayjs.Dayjs | null;
  eliminato?: boolean | null;
  lingua?: Pick<ILingua, 'idLingua'> | null;
}

export type NewFrase = Omit<IFrase, 'id'> & { id: null };
