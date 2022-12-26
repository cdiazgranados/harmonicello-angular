import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPresets, NewPresets } from '../presets.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPresets for edit and NewPresetsFormGroupInput for create.
 */
type PresetsFormGroupInput = IPresets | PartialWithRequiredKeyOf<NewPresets>;

type PresetsFormDefaults = Pick<NewPresets, 'id' | 'sustain'>;

type PresetsFormGroupContent = {
  id: FormControl<IPresets['id'] | NewPresets['id']>;
  presetTitle: FormControl<IPresets['presetTitle']>;
  hertz: FormControl<IPresets['hertz']>;
  sustain: FormControl<IPresets['sustain']>;
  waveform: FormControl<IPresets['waveform']>;
  instrument: FormControl<IPresets['instrument']>;
  user: FormControl<IPresets['user']>;
};

export type PresetsFormGroup = FormGroup<PresetsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PresetsFormService {
  createPresetsFormGroup(presets: PresetsFormGroupInput = { id: null }): PresetsFormGroup {
    const presetsRawValue = {
      ...this.getFormDefaults(),
      ...presets,
    };
    return new FormGroup<PresetsFormGroupContent>({
      id: new FormControl(
        { value: presetsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      presetTitle: new FormControl(presetsRawValue.presetTitle),
      hertz: new FormControl(presetsRawValue.hertz),
      sustain: new FormControl(presetsRawValue.sustain),
      waveform: new FormControl(presetsRawValue.waveform),
      instrument: new FormControl(presetsRawValue.instrument),
      user: new FormControl(presetsRawValue.user),
    });
  }

  getPresets(form: PresetsFormGroup): IPresets | NewPresets {
    return form.getRawValue() as IPresets | NewPresets;
  }

  resetForm(form: PresetsFormGroup, presets: PresetsFormGroupInput): void {
    const presetsRawValue = { ...this.getFormDefaults(), ...presets };
    form.reset(
      {
        ...presetsRawValue,
        id: { value: presetsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PresetsFormDefaults {
    return {
      id: null,
      sustain: false,
    };
  }
}
