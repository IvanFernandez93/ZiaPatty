import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFrase, NewFrase } from '../frase.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFrase for edit and NewFraseFormGroupInput for create.
 */
type FraseFormGroupInput = IFrase | PartialWithRequiredKeyOf<NewFrase>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFrase | NewFrase> = Omit<T, 'dataCreazione' | 'dataUltimaModifica'> & {
  dataCreazione?: string | null;
  dataUltimaModifica?: string | null;
};

type FraseFormRawValue = FormValueOf<IFrase>;

type NewFraseFormRawValue = FormValueOf<NewFrase>;

type FraseFormDefaults = Pick<NewFrase, 'id' | 'dataCreazione' | 'dataUltimaModifica' | 'eliminato'>;

type FraseFormGroupContent = {
  id: FormControl<FraseFormRawValue['id'] | NewFrase['id']>;
  idFrase: FormControl<FraseFormRawValue['idFrase']>;
  frase: FormControl<FraseFormRawValue['frase']>;
  codiceStato: FormControl<FraseFormRawValue['codiceStato']>;
  dataCreazione: FormControl<FraseFormRawValue['dataCreazione']>;
  dataUltimaModifica: FormControl<FraseFormRawValue['dataUltimaModifica']>;
  eliminato: FormControl<FraseFormRawValue['eliminato']>;
  lingua: FormControl<FraseFormRawValue['lingua']>;
};

export type FraseFormGroup = FormGroup<FraseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FraseFormService {
  createFraseFormGroup(frase: FraseFormGroupInput = { id: null }): FraseFormGroup {
    const fraseRawValue = this.convertFraseToFraseRawValue({
      ...this.getFormDefaults(),
      ...frase,
    });
    return new FormGroup<FraseFormGroupContent>({
      id: new FormControl(
        { value: fraseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      idFrase: new FormControl(fraseRawValue.idFrase),
      frase: new FormControl(fraseRawValue.frase),
      codiceStato: new FormControl(fraseRawValue.codiceStato),
      dataCreazione: new FormControl(fraseRawValue.dataCreazione),
      dataUltimaModifica: new FormControl(fraseRawValue.dataUltimaModifica),
      eliminato: new FormControl(fraseRawValue.eliminato),
      lingua: new FormControl(fraseRawValue.lingua),
    });
  }

  getFrase(form: FraseFormGroup): IFrase | NewFrase {
    return this.convertFraseRawValueToFrase(form.getRawValue() as FraseFormRawValue | NewFraseFormRawValue);
  }

  resetForm(form: FraseFormGroup, frase: FraseFormGroupInput): void {
    const fraseRawValue = this.convertFraseToFraseRawValue({ ...this.getFormDefaults(), ...frase });
    form.reset(
      {
        ...fraseRawValue,
        id: { value: fraseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FraseFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataCreazione: currentTime,
      dataUltimaModifica: currentTime,
      eliminato: false,
    };
  }

  private convertFraseRawValueToFrase(rawFrase: FraseFormRawValue | NewFraseFormRawValue): IFrase | NewFrase {
    return {
      ...rawFrase,
      dataCreazione: dayjs(rawFrase.dataCreazione, DATE_TIME_FORMAT),
      dataUltimaModifica: dayjs(rawFrase.dataUltimaModifica, DATE_TIME_FORMAT),
    };
  }

  private convertFraseToFraseRawValue(
    frase: IFrase | (Partial<NewFrase> & FraseFormDefaults)
  ): FraseFormRawValue | PartialWithRequiredKeyOf<NewFraseFormRawValue> {
    return {
      ...frase,
      dataCreazione: frase.dataCreazione ? frase.dataCreazione.format(DATE_TIME_FORMAT) : undefined,
      dataUltimaModifica: frase.dataUltimaModifica ? frase.dataUltimaModifica.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
