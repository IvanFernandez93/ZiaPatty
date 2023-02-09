import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AudioService } from '../service/audio.service';

import { AudioComponent } from './audio.component';

describe('Audio Management Component', () => {
  let comp: AudioComponent;
  let fixture: ComponentFixture<AudioComponent>;
  let service: AudioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'audio', component: AudioComponent }]), HttpClientTestingModule],
      declarations: [AudioComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'idAudio,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'idAudio,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(AudioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AudioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AudioService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ idAudio: 123 }],
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
    expect(comp.audio?.[0]).toEqual(expect.objectContaining({ idAudio: 123 }));
  });

  describe('trackIdAudio', () => {
    it('Should forward to audioService', () => {
      const entity = { idAudio: 123 };
      jest.spyOn(service, 'getAudioIdentifier');
      const idAudio = comp.trackIdAudio(0, entity);
      expect(service.getAudioIdentifier).toHaveBeenCalledWith(entity);
      expect(idAudio).toBe(entity.idAudio);
    });
  });
});
