import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PresetsService } from '../service/presets.service';

import { PresetsComponent } from './presets.component';

describe('Presets Management Component', () => {
  let comp: PresetsComponent;
  let fixture: ComponentFixture<PresetsComponent>;
  let service: PresetsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'presets', component: PresetsComponent }]), HttpClientTestingModule],
      declarations: [PresetsComponent],
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
      .overrideTemplate(PresetsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PresetsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PresetsService);

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
    expect(comp.presets?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to presetsService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getPresetsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPresetsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
