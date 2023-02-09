import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILingua, NewLingua } from '../lingua.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { idLingua: unknown }> = Partial<Omit<T, 'idLingua'>> & { idLingua: T['idLingua'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILingua for edit and NewLinguaFormGroupInput for create.
 */
type LinguaFormGroupInput = ILingua | PartialWithRequiredKeyOf<NewLingua>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILingua | NewLingua> = Omit<T, 'dataCreazione' | 'dataUltimaModifica'> & {
  dataCreazione?: string | null;
  dataUltimaModifica?: string | null;
};

type LinguaFormRawValue = FormValueOf<ILingua>;

type NewLinguaFormRawValue = FormValueOf<NewLingua>;

type LinguaFormDefaults = Pick<NewLingua, 'idLingua' | 'dataCreazione' | 'dataUltimaModifica' | 'eliminato'>;

type LinguaFormGroupContent = {
  idLingua: FormControl<LinguaFormRawValue['idLingua'] | NewLingua['idLingua']>;
  codiceLingua: FormControl<LinguaFormRawValue['codiceLingua']>;
  nomeLingua: FormControl<LinguaFormRawValue['nomeLingua']>;
  dataCreazione: FormControl<LinguaFormRawValue['dataCreazione']>;
  dataUltimaModifica: FormControl<LinguaFormRawValue['dataUltimaModifica']>;
  eliminato: FormControl<LinguaFormRawValue['eliminato']>;
};

export type LinguaFormGroup = FormGroup<LinguaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LinguaFormService {
  createLinguaFormGroup(lingua: LinguaFormGroupInput = { idLingua: null }): LinguaFormGroup {
    const linguaRawValue = this.convertLinguaToLinguaRawValue({
      ...this.getFormDefaults(),
      ...lingua,
    });
    return new FormGroup<LinguaFormGroupContent>({
      idLingua: new FormControl(
        { value: linguaRawValue.idLingua, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codiceLingua: new FormControl(linguaRawValue.codiceLingua),
      nomeLingua: new FormControl(linguaRawValue.nomeLingua),
      dataCreazione: new FormControl(linguaRawValue.dataCreazione),
      dataUltimaModifica: new FormControl(linguaRawValue.dataUltimaModifica),
      eliminato: new FormControl(linguaRawValue.eliminato),
    });
  }

  getLingua(form: LinguaFormGroup): ILingua | NewLingua {
    return this.convertLinguaRawValueToLingua(form.getRawValue() as LinguaFormRawValue | NewLinguaFormRawValue);
  }

  resetForm(form: LinguaFormGroup, lingua: LinguaFormGroupInput): void {
    const linguaRawValue = this.convertLinguaToLinguaRawValue({ ...this.getFormDefaults(), ...lingua });
    form.reset(
      {
        ...linguaRawValue,
        idLingua: { value: linguaRawValue.idLingua, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LinguaFormDefaults {
    const currentTime = dayjs();

    return {
      idLingua: null,
      dataCreazione: currentTime,
      dataUltimaModifica: currentTime,
      eliminato: false,
    };
  }

  private convertLinguaRawValueToLingua(rawLingua: LinguaFormRawValue | NewLinguaFormRawValue): ILingua | NewLingua {
    return {
      ...rawLingua,
      dataCreazione: dayjs(rawLingua.dataCreazione, DATE_TIME_FORMAT),
      dataUltimaModifica: dayjs(rawLingua.dataUltimaModifica, DATE_TIME_FORMAT),
    };
  }

  private convertLinguaToLinguaRawValue(
    lingua: ILingua | (Partial<NewLingua> & LinguaFormDefaults)
  ): LinguaFormRawValue | PartialWithRequiredKeyOf<NewLinguaFormRawValue> {
    return {
      ...lingua,
      dataCreazione: lingua.dataCreazione ? lingua.dataCreazione.format(DATE_TIME_FORMAT) : undefined,
      dataUltimaModifica: lingua.dataUltimaModifica ? lingua.dataUltimaModifica.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
