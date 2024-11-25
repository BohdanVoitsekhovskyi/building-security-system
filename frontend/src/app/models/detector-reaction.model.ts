import { Detector } from "./detector.model";

export type DetectorReaction = {
  detector: Detector;
  detectorAnswer: string;
  detectorReactionTime: Date;
};
