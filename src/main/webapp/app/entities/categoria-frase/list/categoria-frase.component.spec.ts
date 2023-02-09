import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CategoriaFraseService } from '../service/categoria-frase.service';

import { CategoriaFraseComponent } from './categoria-frase.component';

describe('CategoriaFrase Management Component', () => {
  let comp: CategoriaFraseComponent;
  let fixture: ComponentFixture<CategoriaFraseComponent>;
  let service: CategoriaFraseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'categoria-frase', component: CategoriaFraseComponent }]), HttpClientTestingModule],
      declarations: [CategoriaFraseComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(CategoriaFraseComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoriaFraseComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CategoriaFraseService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.categoriaFrases?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to categoriaFraseService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getCategoriaFraseIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCategoriaFraseIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
