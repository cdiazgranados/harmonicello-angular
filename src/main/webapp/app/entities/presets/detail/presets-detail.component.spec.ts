import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PresetsDetailComponent } from './presets-detail.component';

describe('Presets Management Detail Component', () => {
  let comp: PresetsDetailComponent;
  let fixture: ComponentFixture<PresetsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PresetsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ presets: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PresetsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PresetsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load presets on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.presets).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
