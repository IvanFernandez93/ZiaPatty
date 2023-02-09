import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LinguaDetailComponent } from './lingua-detail.component';

describe('Lingua Management Detail Component', () => {
  let comp: LinguaDetailComponent;
  let fixture: ComponentFixture<LinguaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LinguaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ lingua: { idLingua: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LinguaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LinguaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load lingua on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.lingua).toEqual(expect.objectContaining({ idLingua: 123 }));
    });
  });
});
