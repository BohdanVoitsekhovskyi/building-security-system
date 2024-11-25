import { Detector } from "./detector.model";

export type SystemReaction = {
  detector: Detector
  systemAnswer: string;
  reactionTime: Date;
};
