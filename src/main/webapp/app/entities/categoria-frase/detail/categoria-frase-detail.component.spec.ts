import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CategoriaFraseDetailComponent } from './categoria-frase-detail.component';

describe('CategoriaFrase Management Detail Component', () => {
  let comp: CategoriaFraseDetailComponent;
  let fixture: ComponentFixture<CategoriaFraseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CategoriaFraseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ categoriaFrase: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CategoriaFraseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CategoriaFraseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load categoriaFrase on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.categoriaFrase).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
