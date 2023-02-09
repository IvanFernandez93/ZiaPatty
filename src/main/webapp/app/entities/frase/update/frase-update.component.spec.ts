import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FraseFormService } from './frase-form.service';
import { FraseService } from '../service/frase.service';
import { IFrase } from '../frase.model';
import { ILingua } from 'app/entities/lingua/lingua.model';
import { LinguaService } from 'app/entities/lingua/service/lingua.service';

import { FraseUpdateComponent } from './frase-update.component';

describe('Frase Management Update Component', () => {
  let comp: FraseUpdateComponent;
  let fixture: ComponentFixture<FraseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fraseFormService: FraseFormService;
  let fraseService: FraseService;
  let linguaService: LinguaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FraseUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FraseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FraseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fraseFormService = TestBed.inject(FraseFormService);
    fraseService = TestBed.inject(FraseService);
    linguaService = TestBed.inject(LinguaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Lingua query and add missing value', () => {
      const frase: IFrase = { id: 456 };
      const lingua: ILingua = { idLingua: 11501 };
      frase.lingua = lingua;

      const linguaCollection: ILingua[] = [{ idLingua: 37100 }];
      jest.spyOn(linguaService, 'query').mockReturnValue(of(new HttpResponse({ body: linguaCollection })));
      const additionalLinguas = [lingua];
      const expectedCollection: ILingua[] = [...additionalLinguas, ...linguaCollection];
      jest.spyOn(linguaService, 'addLinguaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ frase });
      comp.ngOnInit();

      expect(linguaService.query).toHaveBeenCalled();
      expect(linguaService.addLinguaToCollectionIfMissing).toHaveBeenCalledWith(
        linguaCollection,
        ...additionalLinguas.map(expect.objectContaining)
      );
      expect(comp.linguasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const frase: IFrase = { id: 456 };
      const lingua: ILingua = { idLingua: 98483 };
      frase.lingua = lingua;

      activatedRoute.data = of({ frase });
      comp.ngOnInit();

      expect(comp.linguasSharedCollection).toContain(lingua);
      expect(comp.frase).toEqual(frase);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrase>>();
      const frase = { id: 123 };
      jest.spyOn(fraseFormService, 'getFrase').mockReturnValue(frase);
      jest.spyOn(fraseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frase });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: frase }));
      saveSubject.complete();

      // THEN
      expect(fraseFormService.getFrase).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fraseService.update).toHaveBeenCalledWith(expect.objectContaining(frase));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrase>>();
      const frase = { id: 123 };
      jest.spyOn(fraseFormService, 'getFrase').mockReturnValue({ id: null });
      jest.spyOn(fraseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frase: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: frase }));
      saveSubject.complete();

      // THEN
      expect(fraseFormService.getFrase).toHaveBeenCalled();
      expect(fraseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrase>>();
      const frase = { id: 123 };
      jest.spyOn(fraseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frase });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fraseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLingua', () => {
      it('Should forward to linguaService', () => {
        const entity = { idLingua: 123 };
        const entity2 = { idLingua: 456 };
        jest.spyOn(linguaService, 'compareLingua');
        comp.compareLingua(entity, entity2);
        expect(linguaService.compareLingua).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
