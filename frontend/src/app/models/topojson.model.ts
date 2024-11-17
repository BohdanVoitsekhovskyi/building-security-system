export interface JsonData {
  type: string;
  objects: { [key: string]: GeometryCollection };
  arcs: number[][][];
}

export interface GeometryCollection {
  type: string; // Default value: "GeometryCollection"
  geometries: Geometry[];
}

export interface Geometry {
  type: string; // Default value: "Polygon"
  arcs: number[][];
  properties: Properties;
}

export interface Properties {
  id: number;
  type: string;
}
