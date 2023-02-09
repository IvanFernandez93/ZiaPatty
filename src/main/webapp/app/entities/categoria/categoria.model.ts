import dayjs from 'dayjs/esm';
import { Stato } from 'app/entities/enumerations/stato.model';

export interface ICategoria {
  idCategoria: number;
  nome?: string | null;
  idCategoriaPadre?: number | null;
  codiceStato?: Stato | null;
  dataCreazione?: dayjs.Dayjs | null;
  dataUltimaModifica?: dayjs.Dayjs | null;
  eliminato?: boolean | null;
  categorieFiglie?: Pick<ICategoria, 'idCategoria'> | null;
}

export type NewCategoria = Omit<ICategoria, 'idCategoria'> & { idCategoria: null };
