import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICategoriaFrase, NewCategoriaFrase } from '../categoria-frase.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategoriaFrase for edit and NewCategoriaFraseFormGroupInput for create.
 */
type CategoriaFraseFormGroupInput = ICategoriaFrase | PartialWithRequiredKeyOf<NewCategoriaFrase>;

type CategoriaFraseFormDefaults = Pick<NewCategoriaFrase, 'id'>;

type CategoriaFraseFormGroupContent = {
  id: FormControl<ICategoriaFrase['id'] | NewCategoriaFrase['id']>;
  idCategoria: FormControl<ICategoriaFrase['idCategoria']>;
  idFrase: FormControl<ICategoriaFrase['idFrase']>;
  categoria: FormControl<ICategoriaFrase['categoria']>;
  frase: FormControl<ICategoriaFrase['frase']>;
};

export type CategoriaFraseFormGroup = FormGroup<CategoriaFraseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CategoriaFraseFormService {
  createCategoriaFraseFormGroup(categoriaFrase: CategoriaFraseFormGroupInput = { id: null }): CategoriaFraseFormGroup {
    const categoriaFraseRawValue = {
      ...this.getFormDefaults(),
      ...categoriaFrase,
    };
    return new FormGroup<CategoriaFraseFormGroupContent>({
      id: new FormControl(
        { value: categoriaFraseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      idCategoria: new FormControl(categoriaFraseRawValue.idCategoria),
      idFrase: new FormControl(categoriaFraseRawValue.idFrase),
      categoria: new FormControl(categoriaFraseRawValue.categoria),
      frase: new FormControl(categoriaFraseRawValue.frase),
    });
  }

  getCategoriaFrase(form: CategoriaFraseFormGroup): ICategoriaFrase | NewCategoriaFrase {
    return form.getRawValue() as ICategoriaFrase | NewCategoriaFrase;
  }

  resetForm(form: CategoriaFraseFormGroup, categoriaFrase: CategoriaFraseFormGroupInput): void {
    const categoriaFraseRawValue = { ...this.getFormDefaults(), ...categoriaFrase };
    form.reset(
      {
        ...categoriaFraseRawValue,
        id: { value: categoriaFraseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CategoriaFraseFormDefaults {
    return {
      id: null,
    };
  }
}
