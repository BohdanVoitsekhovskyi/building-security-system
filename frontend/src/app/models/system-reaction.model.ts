import { Detector } from "./detector.model";

export type SystemReaction = {
  detectors: Detector[]
  systemAnswer: string;
  time: Date;
};
