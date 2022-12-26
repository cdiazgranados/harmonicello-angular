import { Waveform } from 'app/entities/enumerations/waveform.model';
import { Instrument } from 'app/entities/enumerations/instrument.model';

import { IPresets, NewPresets } from './presets.model';

export const sampleWithRequiredData: IPresets = {
  id: 49033,
};

export const sampleWithPartialData: IPresets = {
  id: 73523,
  presetTitle: 'Home',
  waveform: Waveform['TRIANGLE'],
  instrument: Instrument['VIOLA'],
};

export const sampleWithFullData: IPresets = {
  id: 63605,
  presetTitle: 'dynamic',
  hertz: 8929,
  sustain: true,
  waveform: Waveform['SQUARE'],
  instrument: Instrument['VIOLA'],
};

export const sampleWithNewData: NewPresets = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
