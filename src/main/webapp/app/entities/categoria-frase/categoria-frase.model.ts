import { ICategoria } from 'app/entities/categoria/categoria.model';
import { IFrase } from 'app/entities/frase/frase.model';

export interface ICategoriaFrase {
  id: number;
  idCategoria?: number | null;
  idFrase?: number | null;
  categoria?: Pick<ICategoria, 'idCategoria'> | null;
  frase?: Pick<IFrase, 'id'> | null;
}

export type NewCategoriaFrase = Omit<ICategoriaFrase, 'id'> & { id: null };
