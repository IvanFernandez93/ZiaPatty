import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { LinguaService } from '../service/lingua.service';

import { LinguaComponent } from './lingua.component';

describe('Lingua Management Component', () => {
  let comp: LinguaComponent;
  let fixture: ComponentFixture<LinguaComponent>;
  let service: LinguaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'lingua', component: LinguaComponent }]), HttpClientTestingModule],
      declarations: [LinguaComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'idLingua,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'idLingua,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(LinguaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LinguaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LinguaService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ idLingua: 123 }],
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
    expect(comp.linguas?.[0]).toEqual(expect.objectContaining({ idLingua: 123 }));
  });

  describe('trackIdLingua', () => {
    it('Should forward to linguaService', () => {
      const entity = { idLingua: 123 };
      jest.spyOn(service, 'getLinguaIdentifier');
      const idLingua = comp.trackIdLingua(0, entity);
      expect(service.getLinguaIdentifier).toHaveBeenCalledWith(entity);
      expect(idLingua).toBe(entity.idLingua);
    });
  });
});
