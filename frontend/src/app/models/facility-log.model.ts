import { SystemReaction } from './system-reaction.model';

export type FacilityLog = {
  id: number;
  logMessages: SystemReaction[];
  isFinished: boolean;
};
