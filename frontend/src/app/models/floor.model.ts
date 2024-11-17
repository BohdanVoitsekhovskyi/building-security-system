import { Detector } from "./detector.model";
import { JsonData } from "./topojson.model";

export type Floor = {
  id: number;
  data: JsonData;
  detectors: Detector[]
};