import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../frase.test-samples';

import { FraseFormService } from './frase-form.service';

describe('Frase Form Service', () => {
  let service: FraseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FraseFormService);
  });

  describe('Service methods', () => {
    describe('createFraseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFraseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idFrase: expect.any(Object),
            frase: expect.any(Object),
            codiceStato: expect.any(Object),
            dataCreazione: expect.any(Object),
            dataUltimaModifica: expect.any(Object),
            eliminato: expect.any(Object),
            lingua: expect.any(Object),
          })
        );
      });

      it('passing IFrase should create a new form with FormGroup', () => {
        const formGroup = service.createFraseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idFrase: expect.any(Object),
            frase: expect.any(Object),
            codiceStato: expect.any(Object),
            dataCreazione: expect.any(Object),
            dataUltimaModifica: expect.any(Object),
            eliminato: expect.any(Object),
            lingua: expect.any(Object),
          })
        );
      });
    });

    describe('getFrase', () => {
      it('should return NewFrase for default Frase initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFraseFormGroup(sampleWithNewData);

        const frase = service.getFrase(formGroup) as any;

        expect(frase).toMatchObject(sampleWithNewData);
      });

      it('should return NewFrase for empty Frase initial value', () => {
        const formGroup = service.createFraseFormGroup();

        const frase = service.getFrase(formGroup) as any;

        expect(frase).toMatchObject({});
      });

      it('should return IFrase', () => {
        const formGroup = service.createFraseFormGroup(sampleWithRequiredData);

        const frase = service.getFrase(formGroup) as any;

        expect(frase).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFrase should not enable id FormControl', () => {
        const formGroup = service.createFraseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFrase should disable id FormControl', () => {
        const formGroup = service.createFraseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
