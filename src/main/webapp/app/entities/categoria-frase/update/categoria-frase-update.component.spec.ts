import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CategoriaFraseFormService } from './categoria-frase-form.service';
import { CategoriaFraseService } from '../service/categoria-frase.service';
import { ICategoriaFrase } from '../categoria-frase.model';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IFrase } from 'app/entities/frase/frase.model';
import { FraseService } from 'app/entities/frase/service/frase.service';

import { CategoriaFraseUpdateComponent } from './categoria-frase-update.component';

describe('CategoriaFrase Management Update Component', () => {
  let comp: CategoriaFraseUpdateComponent;
  let fixture: ComponentFixture<CategoriaFraseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let categoriaFraseFormService: CategoriaFraseFormService;
  let categoriaFraseService: CategoriaFraseService;
  let categoriaService: CategoriaService;
  let fraseService: FraseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CategoriaFraseUpdateComponent],
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
      .overrideTemplate(CategoriaFraseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoriaFraseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    categoriaFraseFormService = TestBed.inject(CategoriaFraseFormService);
    categoriaFraseService = TestBed.inject(CategoriaFraseService);
    categoriaService = TestBed.inject(CategoriaService);
    fraseService = TestBed.inject(FraseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Categoria query and add missing value', () => {
      const categoriaFrase: ICategoriaFrase = { id: 456 };
      const categoria: ICategoria = { idCategoria: 55655 };
      categoriaFrase.categoria = categoria;

      const categoriaCollection: ICategoria[] = [{ idCategoria: 93399 }];
      jest.spyOn(categoriaService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaCollection })));
      const additionalCategorias = [categoria];
      const expectedCollection: ICategoria[] = [...additionalCategorias, ...categoriaCollection];
      jest.spyOn(categoriaService, 'addCategoriaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ categoriaFrase });
      comp.ngOnInit();

      expect(categoriaService.query).toHaveBeenCalled();
      expect(categoriaService.addCategoriaToCollectionIfMissing).toHaveBeenCalledWith(
        categoriaCollection,
        ...additionalCategorias.map(expect.objectContaining)
      );
      expect(comp.categoriasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Frase query and add missing value', () => {
      const categoriaFrase: ICategoriaFrase = { id: 456 };
      const frase: IFrase = { id: 396 };
      categoriaFrase.frase = frase;

      const fraseCollection: IFrase[] = [{ id: 2959 }];
      jest.spyOn(fraseService, 'query').mockReturnValue(of(new HttpResponse({ body: fraseCollection })));
      const additionalFrases = [frase];
      const expectedCollection: IFrase[] = [...additionalFrases, ...fraseCollection];
      jest.spyOn(fraseService, 'addFraseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ categoriaFrase });
      comp.ngOnInit();

      expect(fraseService.query).toHaveBeenCalled();
      expect(fraseService.addFraseToCollectionIfMissing).toHaveBeenCalledWith(
        fraseCollection,
        ...additionalFrases.map(expect.objectContaining)
      );
      expect(comp.frasesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const categoriaFrase: ICategoriaFrase = { id: 456 };
      const categoria: ICategoria = { idCategoria: 76204 };
      categoriaFrase.categoria = categoria;
      const frase: IFrase = { id: 15947 };
      categoriaFrase.frase = frase;

      activatedRoute.data = of({ categoriaFrase });
      comp.ngOnInit();

      expect(comp.categoriasSharedCollection).toContain(categoria);
      expect(comp.frasesSharedCollection).toContain(frase);
      expect(comp.categoriaFrase).toEqual(categoriaFrase);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoriaFrase>>();
      const categoriaFrase = { id: 123 };
      jest.spyOn(categoriaFraseFormService, 'getCategoriaFrase').mockReturnValue(categoriaFrase);
      jest.spyOn(categoriaFraseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaFrase });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaFrase }));
      saveSubject.complete();

      // THEN
      expect(categoriaFraseFormService.getCategoriaFrase).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(categoriaFraseService.update).toHaveBeenCalledWith(expect.objectContaining(categoriaFrase));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoriaFrase>>();
      const categoriaFrase = { id: 123 };
      jest.spyOn(categoriaFraseFormService, 'getCategoriaFrase').mockReturnValue({ id: null });
      jest.spyOn(categoriaFraseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaFrase: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaFrase }));
      saveSubject.complete();

      // THEN
      expect(categoriaFraseFormService.getCategoriaFrase).toHaveBeenCalled();
      expect(categoriaFraseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoriaFrase>>();
      const categoriaFrase = { id: 123 };
      jest.spyOn(categoriaFraseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaFrase });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(categoriaFraseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCategoria', () => {
      it('Should forward to categoriaService', () => {
        const entity = { idCategoria: 123 };
        const entity2 = { idCategoria: 456 };
        jest.spyOn(categoriaService, 'compareCategoria');
        comp.compareCategoria(entity, entity2);
        expect(categoriaService.compareCategoria).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFrase', () => {
      it('Should forward to fraseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(fraseService, 'compareFrase');
        comp.compareFrase(entity, entity2);
        expect(fraseService.compareFrase).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
