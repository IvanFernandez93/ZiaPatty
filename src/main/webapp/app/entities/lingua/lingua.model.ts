import dayjs from 'dayjs/esm';

export interface ILingua {
  idLingua: number;
  codiceLingua?: string | null;
  nomeLingua?: string | null;
  dataCreazione?: dayjs.Dayjs | null;
  dataUltimaModifica?: dayjs.Dayjs | null;
  eliminato?: boolean | null;
}

export type NewLingua = Omit<ILingua, 'idLingua'> & { idLingua: null };
