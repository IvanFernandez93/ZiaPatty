import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICategoria, NewCategoria } from '../categoria.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { idCategoria: unknown }> = Partial<Omit<T, 'idCategoria'>> & { idCategoria: T['idCategoria'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategoria for edit and NewCategoriaFormGroupInput for create.
 */
type CategoriaFormGroupInput = ICategoria | PartialWithRequiredKeyOf<NewCategoria>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICategoria | NewCategoria> = Omit<T, 'dataCreazione' | 'dataUltimaModifica'> & {
  dataCreazione?: string | null;
  dataUltimaModifica?: string | null;
};

type CategoriaFormRawValue = FormValueOf<ICategoria>;

type NewCategoriaFormRawValue = FormValueOf<NewCategoria>;

type CategoriaFormDefaults = Pick<NewCategoria, 'idCategoria' | 'dataCreazione' | 'dataUltimaModifica' | 'eliminato'>;

type CategoriaFormGroupContent = {
  idCategoria: FormControl<CategoriaFormRawValue['idCategoria'] | NewCategoria['idCategoria']>;
  nome: FormControl<CategoriaFormRawValue['nome']>;
  idCategoriaPadre: FormControl<CategoriaFormRawValue['idCategoriaPadre']>;
  codiceStato: FormControl<CategoriaFormRawValue['codiceStato']>;
  dataCreazione: FormControl<CategoriaFormRawValue['dataCreazione']>;
  dataUltimaModifica: FormControl<CategoriaFormRawValue['dataUltimaModifica']>;
  eliminato: FormControl<CategoriaFormRawValue['eliminato']>;
  categorieFiglie: FormControl<CategoriaFormRawValue['categorieFiglie']>;
};

export type CategoriaFormGroup = FormGroup<CategoriaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CategoriaFormService {
  createCategoriaFormGroup(categoria: CategoriaFormGroupInput = { idCategoria: null }): CategoriaFormGroup {
    const categoriaRawValue = this.convertCategoriaToCategoriaRawValue({
      ...this.getFormDefaults(),
      ...categoria,
    });
    return new FormGroup<CategoriaFormGroupContent>({
      idCategoria: new FormControl(
        { value: categoriaRawValue.idCategoria, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nome: new FormControl(categoriaRawValue.nome),
      idCategoriaPadre: new FormControl(categoriaRawValue.idCategoriaPadre),
      codiceStato: new FormControl(categoriaRawValue.codiceStato),
      dataCreazione: new FormControl(categoriaRawValue.dataCreazione),
      dataUltimaModifica: new FormControl(categoriaRawValue.dataUltimaModifica),
      eliminato: new FormControl(categoriaRawValue.eliminato),
      categorieFiglie: new FormControl(categoriaRawValue.categorieFiglie),
    });
  }

  getCategoria(form: CategoriaFormGroup): ICategoria | NewCategoria {
    return this.convertCategoriaRawValueToCategoria(form.getRawValue() as CategoriaFormRawValue | NewCategoriaFormRawValue);
  }

  resetForm(form: CategoriaFormGroup, categoria: CategoriaFormGroupInput): void {
    const categoriaRawValue = this.convertCategoriaToCategoriaRawValue({ ...this.getFormDefaults(), ...categoria });
    form.reset(
      {
        ...categoriaRawValue,
        idCategoria: { value: categoriaRawValue.idCategoria, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CategoriaFormDefaults {
    const currentTime = dayjs();

    return {
      idCategoria: null,
      dataCreazione: currentTime,
      dataUltimaModifica: currentTime,
      eliminato: false,
    };
  }

  private convertCategoriaRawValueToCategoria(rawCategoria: CategoriaFormRawValue | NewCategoriaFormRawValue): ICategoria | NewCategoria {
    return {
      ...rawCategoria,
      dataCreazione: dayjs(rawCategoria.dataCreazione, DATE_TIME_FORMAT),
      dataUltimaModifica: dayjs(rawCategoria.dataUltimaModifica, DATE_TIME_FORMAT),
    };
  }

  private convertCategoriaToCategoriaRawValue(
    categoria: ICategoria | (Partial<NewCategoria> & CategoriaFormDefaults)
  ): CategoriaFormRawValue | PartialWithRequiredKeyOf<NewCategoriaFormRawValue> {
    return {
      ...categoria,
      dataCreazione: categoria.dataCreazione ? categoria.dataCreazione.format(DATE_TIME_FORMAT) : undefined,
      dataUltimaModifica: categoria.dataUltimaModifica ? categoria.dataUltimaModifica.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
