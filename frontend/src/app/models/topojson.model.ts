export interface JsonData {
  type: string;
  objects: { [key: string]: GeometryCollection };
  arcs: number[][][];
}

export interface GeometryCollection {
  type: string;
  geometries: Geometry[];
}

export interface Geometry {
  type: string;
  arcs: number[][];
  properties: Properties;
}

export interface Properties {
  id: number;
  type: string;
}
