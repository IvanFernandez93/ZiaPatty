import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFraseAudio, NewFraseAudio } from '../frase-audio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFraseAudio for edit and NewFraseAudioFormGroupInput for create.
 */
type FraseAudioFormGroupInput = IFraseAudio | PartialWithRequiredKeyOf<NewFraseAudio>;

type FraseAudioFormDefaults = Pick<NewFraseAudio, 'id'>;

type FraseAudioFormGroupContent = {
  id: FormControl<IFraseAudio['id'] | NewFraseAudio['id']>;
  idCategoria: FormControl<IFraseAudio['idCategoria']>;
  idFrase: FormControl<IFraseAudio['idFrase']>;
  frase: FormControl<IFraseAudio['frase']>;
  audio: FormControl<IFraseAudio['audio']>;
};

export type FraseAudioFormGroup = FormGroup<FraseAudioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FraseAudioFormService {
  createFraseAudioFormGroup(fraseAudio: FraseAudioFormGroupInput = { id: null }): FraseAudioFormGroup {
    const fraseAudioRawValue = {
      ...this.getFormDefaults(),
      ...fraseAudio,
    };
    return new FormGroup<FraseAudioFormGroupContent>({
      id: new FormControl(
        { value: fraseAudioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      idCategoria: new FormControl(fraseAudioRawValue.idCategoria),
      idFrase: new FormControl(fraseAudioRawValue.idFrase),
      frase: new FormControl(fraseAudioRawValue.frase),
      audio: new FormControl(fraseAudioRawValue.audio),
    });
  }

  getFraseAudio(form: FraseAudioFormGroup): IFraseAudio | NewFraseAudio {
    return form.getRawValue() as IFraseAudio | NewFraseAudio;
  }

  resetForm(form: FraseAudioFormGroup, fraseAudio: FraseAudioFormGroupInput): void {
    const fraseAudioRawValue = { ...this.getFormDefaults(), ...fraseAudio };
    form.reset(
      {
        ...fraseAudioRawValue,
        id: { value: fraseAudioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FraseAudioFormDefaults {
    return {
      id: null,
    };
  }
}
