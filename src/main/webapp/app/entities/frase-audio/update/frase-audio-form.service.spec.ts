import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../frase-audio.test-samples';

import { FraseAudioFormService } from './frase-audio-form.service';

describe('FraseAudio Form Service', () => {
  let service: FraseAudioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FraseAudioFormService);
  });

  describe('Service methods', () => {
    describe('createFraseAudioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFraseAudioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idCategoria: expect.any(Object),
            idFrase: expect.any(Object),
            frase: expect.any(Object),
            audio: expect.any(Object),
          })
        );
      });

      it('passing IFraseAudio should create a new form with FormGroup', () => {
        const formGroup = service.createFraseAudioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idCategoria: expect.any(Object),
            idFrase: expect.any(Object),
            frase: expect.any(Object),
            audio: expect.any(Object),
          })
        );
      });
    });

    describe('getFraseAudio', () => {
      it('should return NewFraseAudio for default FraseAudio initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFraseAudioFormGroup(sampleWithNewData);

        const fraseAudio = service.getFraseAudio(formGroup) as any;

        expect(fraseAudio).toMatchObject(sampleWithNewData);
      });

      it('should return NewFraseAudio for empty FraseAudio initial value', () => {
        const formGroup = service.createFraseAudioFormGroup();

        const fraseAudio = service.getFraseAudio(formGroup) as any;

        expect(fraseAudio).toMatchObject({});
      });

      it('should return IFraseAudio', () => {
        const formGroup = service.createFraseAudioFormGroup(sampleWithRequiredData);

        const fraseAudio = service.getFraseAudio(formGroup) as any;

        expect(fraseAudio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFraseAudio should not enable id FormControl', () => {
        const formGroup = service.createFraseAudioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFraseAudio should disable id FormControl', () => {
        const formGroup = service.createFraseAudioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
