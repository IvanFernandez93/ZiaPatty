import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LinguaFormService } from './lingua-form.service';
import { LinguaService } from '../service/lingua.service';
import { ILingua } from '../lingua.model';

import { LinguaUpdateComponent } from './lingua-update.component';

describe('Lingua Management Update Component', () => {
  let comp: LinguaUpdateComponent;
  let fixture: ComponentFixture<LinguaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let linguaFormService: LinguaFormService;
  let linguaService: LinguaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LinguaUpdateComponent],
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
      .overrideTemplate(LinguaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LinguaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    linguaFormService = TestBed.inject(LinguaFormService);
    linguaService = TestBed.inject(LinguaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const lingua: ILingua = { idLingua: 456 };

      activatedRoute.data = of({ lingua });
      comp.ngOnInit();

      expect(comp.lingua).toEqual(lingua);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILingua>>();
      const lingua = { idLingua: 123 };
      jest.spyOn(linguaFormService, 'getLingua').mockReturnValue(lingua);
      jest.spyOn(linguaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lingua });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lingua }));
      saveSubject.complete();

      // THEN
      expect(linguaFormService.getLingua).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(linguaService.update).toHaveBeenCalledWith(expect.objectContaining(lingua));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILingua>>();
      const lingua = { idLingua: 123 };
      jest.spyOn(linguaFormService, 'getLingua').mockReturnValue({ idLingua: null });
      jest.spyOn(linguaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lingua: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lingua }));
      saveSubject.complete();

      // THEN
      expect(linguaFormService.getLingua).toHaveBeenCalled();
      expect(linguaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILingua>>();
      const lingua = { idLingua: 123 };
      jest.spyOn(linguaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lingua });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(linguaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
