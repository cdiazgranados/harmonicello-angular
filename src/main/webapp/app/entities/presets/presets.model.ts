import { IUser } from 'app/entities/user/user.model';
import { Waveform } from 'app/entities/enumerations/waveform.model';
import { Instrument } from 'app/entities/enumerations/instrument.model';

export interface IPresets {
  id: number;
  presetTitle?: string | null;
  hertz?: number | null;
  sustain?: boolean | null;
  waveform?: Waveform | null;
  instrument?: Instrument | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewPresets = Omit<IPresets, 'id'> & { id: null };
