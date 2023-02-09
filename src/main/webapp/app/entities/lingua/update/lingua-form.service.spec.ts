import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../lingua.test-samples';

import { LinguaFormService } from './lingua-form.service';

describe('Lingua Form Service', () => {
  let service: LinguaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LinguaFormService);
  });

  describe('Service methods', () => {
    describe('createLinguaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLinguaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            idLingua: expect.any(Object),
            codiceLingua: expect.any(Object),
            nomeLingua: expect.any(Object),
            dataCreazione: expect.any(Object),
            dataUltimaModifica: expect.any(Object),
            eliminato: expect.any(Object),
          })
        );
      });

      it('passing ILingua should create a new form with FormGroup', () => {
        const formGroup = service.createLinguaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            idLingua: expect.any(Object),
            codiceLingua: expect.any(Object),
            nomeLingua: expect.any(Object),
            dataCreazione: expect.any(Object),
            dataUltimaModifica: expect.any(Object),
            eliminato: expect.any(Object),
          })
        );
      });
    });

    describe('getLingua', () => {
      it('should return NewLingua for default Lingua initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLinguaFormGroup(sampleWithNewData);

        const lingua = service.getLingua(formGroup) as any;

        expect(lingua).toMatchObject(sampleWithNewData);
      });

      it('should return NewLingua for empty Lingua initial value', () => {
        const formGroup = service.createLinguaFormGroup();

        const lingua = service.getLingua(formGroup) as any;

        expect(lingua).toMatchObject({});
      });

      it('should return ILingua', () => {
        const formGroup = service.createLinguaFormGroup(sampleWithRequiredData);

        const lingua = service.getLingua(formGroup) as any;

        expect(lingua).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILingua should not enable idLingua FormControl', () => {
        const formGroup = service.createLinguaFormGroup();
        expect(formGroup.controls.idLingua.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.idLingua.disabled).toBe(true);
      });

      it('passing NewLingua should disable idLingua FormControl', () => {
        const formGroup = service.createLinguaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.idLingua.disabled).toBe(true);

        service.resetForm(formGroup, { idLingua: null });

        expect(formGroup.controls.idLingua.disabled).toBe(true);
      });
    });
  });
});
