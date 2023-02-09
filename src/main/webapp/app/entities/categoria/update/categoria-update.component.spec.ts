import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CategoriaFormService } from './categoria-form.service';
import { CategoriaService } from '../service/categoria.service';
import { ICategoria } from '../categoria.model';

import { CategoriaUpdateComponent } from './categoria-update.component';

describe('Categoria Management Update Component', () => {
  let comp: CategoriaUpdateComponent;
  let fixture: ComponentFixture<CategoriaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let categoriaFormService: CategoriaFormService;
  let categoriaService: CategoriaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CategoriaUpdateComponent],
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
      .overrideTemplate(CategoriaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoriaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    categoriaFormService = TestBed.inject(CategoriaFormService);
    categoriaService = TestBed.inject(CategoriaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Categoria query and add missing value', () => {
      const categoria: ICategoria = { idCategoria: 456 };
      const categorieFiglie: ICategoria = { idCategoria: 55770 };
      categoria.categorieFiglie = categorieFiglie;

      const categoriaCollection: ICategoria[] = [{ idCategoria: 9997 }];
      jest.spyOn(categoriaService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaCollection })));
      const additionalCategorias = [categorieFiglie];
      const expectedCollection: ICategoria[] = [...additionalCategorias, ...categoriaCollection];
      jest.spyOn(categoriaService, 'addCategoriaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ categoria });
      comp.ngOnInit();

      expect(categoriaService.query).toHaveBeenCalled();
      expect(categoriaService.addCategoriaToCollectionIfMissing).toHaveBeenCalledWith(
        categoriaCollection,
        ...additionalCategorias.map(expect.objectContaining)
      );
      expect(comp.categoriasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const categoria: ICategoria = { idCategoria: 456 };
      const categorieFiglie: ICategoria = { idCategoria: 70796 };
      categoria.categorieFiglie = categorieFiglie;

      activatedRoute.data = of({ categoria });
      comp.ngOnInit();

      expect(comp.categoriasSharedCollection).toContain(categorieFiglie);
      expect(comp.categoria).toEqual(categoria);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoria>>();
      const categoria = { idCategoria: 123 };
      jest.spyOn(categoriaFormService, 'getCategoria').mockReturnValue(categoria);
      jest.spyOn(categoriaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoria }));
      saveSubject.complete();

      // THEN
      expect(categoriaFormService.getCategoria).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(categoriaService.update).toHaveBeenCalledWith(expect.objectContaining(categoria));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoria>>();
      const categoria = { idCategoria: 123 };
      jest.spyOn(categoriaFormService, 'getCategoria').mockReturnValue({ idCategoria: null });
      jest.spyOn(categoriaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoria: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoria }));
      saveSubject.complete();

      // THEN
      expect(categoriaFormService.getCategoria).toHaveBeenCalled();
      expect(categoriaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoria>>();
      const categoria = { idCategoria: 123 };
      jest.spyOn(categoriaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(categoriaService.update).toHaveBeenCalled();
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
  });
});
