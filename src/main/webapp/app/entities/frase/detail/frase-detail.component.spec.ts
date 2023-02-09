import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FraseDetailComponent } from './frase-detail.component';

describe('Frase Management Detail Component', () => {
  let comp: FraseDetailComponent;
  let fixture: ComponentFixture<FraseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FraseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ frase: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FraseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FraseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load frase on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.frase).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
