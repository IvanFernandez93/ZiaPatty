import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAudio, NewAudio } from '../audio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { idAudio: unknown }> = Partial<Omit<T, 'idAudio'>> & { idAudio: T['idAudio'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAudio for edit and NewAudioFormGroupInput for create.
 */
type AudioFormGroupInput = IAudio | PartialWithRequiredKeyOf<NewAudio>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAudio | NewAudio> = Omit<T, 'dataCreazione' | 'dataUltimaModifica'> & {
  dataCreazione?: string | null;
  dataUltimaModifica?: string | null;
};

type AudioFormRawValue = FormValueOf<IAudio>;

type NewAudioFormRawValue = FormValueOf<NewAudio>;

type AudioFormDefaults = Pick<NewAudio, 'idAudio' | 'dataCreazione' | 'dataUltimaModifica' | 'eliminato'>;

type AudioFormGroupContent = {
  idAudio: FormControl<AudioFormRawValue['idAudio'] | NewAudio['idAudio']>;
  nome: FormControl<AudioFormRawValue['nome']>;
  codiceStato: FormControl<AudioFormRawValue['codiceStato']>;
  track: FormControl<AudioFormRawValue['track']>;
  trackContentType: FormControl<AudioFormRawValue['trackContentType']>;
  dataCreazione: FormControl<AudioFormRawValue['dataCreazione']>;
  dataUltimaModifica: FormControl<AudioFormRawValue['dataUltimaModifica']>;
  eliminato: FormControl<AudioFormRawValue['eliminato']>;
};

export type AudioFormGroup = FormGroup<AudioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AudioFormService {
  createAudioFormGroup(audio: AudioFormGroupInput = { idAudio: null }): AudioFormGroup {
    const audioRawValue = this.convertAudioToAudioRawValue({
      ...this.getFormDefaults(),
      ...audio,
    });
    return new FormGroup<AudioFormGroupContent>({
      idAudio: new FormControl(
        { value: audioRawValue.idAudio, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nome: new FormControl(audioRawValue.nome),
      codiceStato: new FormControl(audioRawValue.codiceStato),
      track: new FormControl(audioRawValue.track),
      trackContentType: new FormControl(audioRawValue.trackContentType),
      dataCreazione: new FormControl(audioRawValue.dataCreazione),
      dataUltimaModifica: new FormControl(audioRawValue.dataUltimaModifica),
      eliminato: new FormControl(audioRawValue.eliminato),
    });
  }

  getAudio(form: AudioFormGroup): IAudio | NewAudio {
    return this.convertAudioRawValueToAudio(form.getRawValue() as AudioFormRawValue | NewAudioFormRawValue);
  }

  resetForm(form: AudioFormGroup, audio: AudioFormGroupInput): void {
    const audioRawValue = this.convertAudioToAudioRawValue({ ...this.getFormDefaults(), ...audio });
    form.reset(
      {
        ...audioRawValue,
        idAudio: { value: audioRawValue.idAudio, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AudioFormDefaults {
    const currentTime = dayjs();

    return {
      idAudio: null,
      dataCreazione: currentTime,
      dataUltimaModifica: currentTime,
      eliminato: false,
    };
  }

  private convertAudioRawValueToAudio(rawAudio: AudioFormRawValue | NewAudioFormRawValue): IAudio | NewAudio {
    return {
      ...rawAudio,
      dataCreazione: dayjs(rawAudio.dataCreazione, DATE_TIME_FORMAT),
      dataUltimaModifica: dayjs(rawAudio.dataUltimaModifica, DATE_TIME_FORMAT),
    };
  }

  private convertAudioToAudioRawValue(
    audio: IAudio | (Partial<NewAudio> & AudioFormDefaults)
  ): AudioFormRawValue | PartialWithRequiredKeyOf<NewAudioFormRawValue> {
    return {
      ...audio,
      dataCreazione: audio.dataCreazione ? audio.dataCreazione.format(DATE_TIME_FORMAT) : undefined,
      dataUltimaModifica: audio.dataUltimaModifica ? audio.dataUltimaModifica.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
