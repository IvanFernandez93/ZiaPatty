import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FraseAudioFormService } from './frase-audio-form.service';
import { FraseAudioService } from '../service/frase-audio.service';
import { IFraseAudio } from '../frase-audio.model';
import { IFrase } from 'app/entities/frase/frase.model';
import { FraseService } from 'app/entities/frase/service/frase.service';
import { IAudio } from 'app/entities/audio/audio.model';
import { AudioService } from 'app/entities/audio/service/audio.service';

import { FraseAudioUpdateComponent } from './frase-audio-update.component';

describe('FraseAudio Management Update Component', () => {
  let comp: FraseAudioUpdateComponent;
  let fixture: ComponentFixture<FraseAudioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fraseAudioFormService: FraseAudioFormService;
  let fraseAudioService: FraseAudioService;
  let fraseService: FraseService;
  let audioService: AudioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FraseAudioUpdateComponent],
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
      .overrideTemplate(FraseAudioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FraseAudioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fraseAudioFormService = TestBed.inject(FraseAudioFormService);
    fraseAudioService = TestBed.inject(FraseAudioService);
    fraseService = TestBed.inject(FraseService);
    audioService = TestBed.inject(AudioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Frase query and add missing value', () => {
      const fraseAudio: IFraseAudio = { id: 456 };
      const frase: IFrase = { id: 793 };
      fraseAudio.frase = frase;

      const fraseCollection: IFrase[] = [{ id: 14223 }];
      jest.spyOn(fraseService, 'query').mockReturnValue(of(new HttpResponse({ body: fraseCollection })));
      const additionalFrases = [frase];
      const expectedCollection: IFrase[] = [...additionalFrases, ...fraseCollection];
      jest.spyOn(fraseService, 'addFraseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fraseAudio });
      comp.ngOnInit();

      expect(fraseService.query).toHaveBeenCalled();
      expect(fraseService.addFraseToCollectionIfMissing).toHaveBeenCalledWith(
        fraseCollection,
        ...additionalFrases.map(expect.objectContaining)
      );
      expect(comp.frasesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Audio query and add missing value', () => {
      const fraseAudio: IFraseAudio = { id: 456 };
      const audio: IAudio = { idAudio: 23880 };
      fraseAudio.audio = audio;

      const audioCollection: IAudio[] = [{ idAudio: 69683 }];
      jest.spyOn(audioService, 'query').mockReturnValue(of(new HttpResponse({ body: audioCollection })));
      const additionalAudio = [audio];
      const expectedCollection: IAudio[] = [...additionalAudio, ...audioCollection];
      jest.spyOn(audioService, 'addAudioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fraseAudio });
      comp.ngOnInit();

      expect(audioService.query).toHaveBeenCalled();
      expect(audioService.addAudioToCollectionIfMissing).toHaveBeenCalledWith(
        audioCollection,
        ...additionalAudio.map(expect.objectContaining)
      );
      expect(comp.audioSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const fraseAudio: IFraseAudio = { id: 456 };
      const frase: IFrase = { id: 58839 };
      fraseAudio.frase = frase;
      const audio: IAudio = { idAudio: 95560 };
      fraseAudio.audio = audio;

      activatedRoute.data = of({ fraseAudio });
      comp.ngOnInit();

      expect(comp.frasesSharedCollection).toContain(frase);
      expect(comp.audioSharedCollection).toContain(audio);
      expect(comp.fraseAudio).toEqual(fraseAudio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFraseAudio>>();
      const fraseAudio = { id: 123 };
      jest.spyOn(fraseAudioFormService, 'getFraseAudio').mockReturnValue(fraseAudio);
      jest.spyOn(fraseAudioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fraseAudio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fraseAudio }));
      saveSubject.complete();

      // THEN
      expect(fraseAudioFormService.getFraseAudio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fraseAudioService.update).toHaveBeenCalledWith(expect.objectContaining(fraseAudio));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFraseAudio>>();
      const fraseAudio = { id: 123 };
      jest.spyOn(fraseAudioFormService, 'getFraseAudio').mockReturnValue({ id: null });
      jest.spyOn(fraseAudioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fraseAudio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fraseAudio }));
      saveSubject.complete();

      // THEN
      expect(fraseAudioFormService.getFraseAudio).toHaveBeenCalled();
      expect(fraseAudioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFraseAudio>>();
      const fraseAudio = { id: 123 };
      jest.spyOn(fraseAudioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fraseAudio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fraseAudioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFrase', () => {
      it('Should forward to fraseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(fraseService, 'compareFrase');
        comp.compareFrase(entity, entity2);
        expect(fraseService.compareFrase).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAudio', () => {
      it('Should forward to audioService', () => {
        const entity = { idAudio: 123 };
        const entity2 = { idAudio: 456 };
        jest.spyOn(audioService, 'compareAudio');
        comp.compareAudio(entity, entity2);
        expect(audioService.compareAudio).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
