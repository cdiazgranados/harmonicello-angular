import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../presets.test-samples';

import { PresetsFormService } from './presets-form.service';

describe('Presets Form Service', () => {
  let service: PresetsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PresetsFormService);
  });

  describe('Service methods', () => {
    describe('createPresetsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPresetsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            presetTitle: expect.any(Object),
            hertz: expect.any(Object),
            sustain: expect.any(Object),
            waveform: expect.any(Object),
            instrument: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IPresets should create a new form with FormGroup', () => {
        const formGroup = service.createPresetsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            presetTitle: expect.any(Object),
            hertz: expect.any(Object),
            sustain: expect.any(Object),
            waveform: expect.any(Object),
            instrument: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getPresets', () => {
      it('should return NewPresets for default Presets initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPresetsFormGroup(sampleWithNewData);

        const presets = service.getPresets(formGroup) as any;

        expect(presets).toMatchObject(sampleWithNewData);
      });

      it('should return NewPresets for empty Presets initial value', () => {
        const formGroup = service.createPresetsFormGroup();

        const presets = service.getPresets(formGroup) as any;

        expect(presets).toMatchObject({});
      });

      it('should return IPresets', () => {
        const formGroup = service.createPresetsFormGroup(sampleWithRequiredData);

        const presets = service.getPresets(formGroup) as any;

        expect(presets).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPresets should not enable id FormControl', () => {
        const formGroup = service.createPresetsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPresets should disable id FormControl', () => {
        const formGroup = service.createPresetsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
