import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../categoria-frase.test-samples';

import { CategoriaFraseFormService } from './categoria-frase-form.service';

describe('CategoriaFrase Form Service', () => {
  let service: CategoriaFraseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CategoriaFraseFormService);
  });

  describe('Service methods', () => {
    describe('createCategoriaFraseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCategoriaFraseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idCategoria: expect.any(Object),
            idFrase: expect.any(Object),
            categoria: expect.any(Object),
            frase: expect.any(Object),
          })
        );
      });

      it('passing ICategoriaFrase should create a new form with FormGroup', () => {
        const formGroup = service.createCategoriaFraseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idCategoria: expect.any(Object),
            idFrase: expect.any(Object),
            categoria: expect.any(Object),
            frase: expect.any(Object),
          })
        );
      });
    });

    describe('getCategoriaFrase', () => {
      it('should return NewCategoriaFrase for default CategoriaFrase initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCategoriaFraseFormGroup(sampleWithNewData);

        const categoriaFrase = service.getCategoriaFrase(formGroup) as any;

        expect(categoriaFrase).toMatchObject(sampleWithNewData);
      });

      it('should return NewCategoriaFrase for empty CategoriaFrase initial value', () => {
        const formGroup = service.createCategoriaFraseFormGroup();

        const categoriaFrase = service.getCategoriaFrase(formGroup) as any;

        expect(categoriaFrase).toMatchObject({});
      });

      it('should return ICategoriaFrase', () => {
        const formGroup = service.createCategoriaFraseFormGroup(sampleWithRequiredData);

        const categoriaFrase = service.getCategoriaFrase(formGroup) as any;

        expect(categoriaFrase).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICategoriaFrase should not enable id FormControl', () => {
        const formGroup = service.createCategoriaFraseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCategoriaFrase should disable id FormControl', () => {
        const formGroup = service.createCategoriaFraseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
