import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FraseAudioDetailComponent } from './frase-audio-detail.component';

describe('FraseAudio Management Detail Component', () => {
  let comp: FraseAudioDetailComponent;
  let fixture: ComponentFixture<FraseAudioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FraseAudioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fraseAudio: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FraseAudioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FraseAudioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fraseAudio on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fraseAudio).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
