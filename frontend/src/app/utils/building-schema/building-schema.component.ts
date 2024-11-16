import { Component, ElementRef, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import * as d3 from 'd3';
import * as topojson from 'topojson-client';
import { sensors } from './dummy-data';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-building-schema',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './building-schema.component.html',
  styleUrl: './building-schema.component.css',
})
export class BuildingSchemaComponent {
  @Output() onAddSensor = new EventEmitter();
  sensors: { id: number; pos: { x: number; y: number }; type: string }[] = [];
  projection: any;
  private readonly canvas = { w: 1000, h: 1000 };

  sensorTypes = sensors;
  contextMenuVisible = false;
  contextMenuPos?: { x: number; y: number };
  sensorData?: any;

  constructor(private el: ElementRef) {}

  ngOnInit(): void {
    d3.json('example/testbuilding.json').then((data: any) => {
      this.renderMap(data);
    });
  }

  private renderMap(topoData: any): void {
    const geoData: Record<string, any> = {};
    const arrOfKeys = Object.keys(topoData.objects);

    // Конвертуємо TopoJSON в GeoJSON
    arrOfKeys.forEach((key) => {
      geoData[key] = topojson.feature(topoData, topoData.objects[key]);
    });

    arrOfKeys.push('sensors');

    const d3Identity = d3.geoIdentity();
    this.projection = d3Identity.fitSize(
      [this.canvas.w, this.canvas.h],
      geoData['apartment']
    );
    const d3Path = d3.geoPath(this.projection);

    const svgContainer = d3
      .select(this.el.nativeElement.querySelector('#svg_container'))
      .append('svg')
      .attr('viewBox', `0 0 ${this.canvas.w} ${this.canvas.h}`)
      .classed('floormap', true);

    const groups = svgContainer
      .selectAll('g')
      .data(arrOfKeys)
      .enter()
      .append('g')
      .attr('class', (d) => d);

    groups
      .selectAll('path')
      .data((d) => geoData[d]?.features || [])
      .enter()
      .append('path')
      .attr('d', (feature) => d3Path(feature as d3.GeoPermissibleObjects) || '')
      .on('click', (e: any, d: any) => {
        if (d.properties.type === 'window' || d.properties.type === 'door' || d.properties.type === 'room')
          return this.showContextSensor(e, d);
      });
  }

  showContextSensor(event: any, data: any) {
    if (this.sensors.find((s) => s.id === data.properties.id)) return;

    this.contextMenuVisible = true;
    this.contextMenuPos = { x: event.x, y: event.y };
    this.sensorData = data;
    console.log(this.contextMenuPos);
  }

  addSensor(event: Event) {
    const name = (event.target as HTMLDivElement).id;
    const coords: [number, number] = d3.polygonCentroid(
      this.sensorData.geometry.coordinates[0]
    );

    const projectedCoords = this.projection(coords);

    const size = 50;
    d3.select('.sensors')
      .append('image')
      .attr('x', projectedCoords[0] - size / 2)
      .attr('y', projectedCoords[1] - size / 2)
      .attr('width', size)
      .attr('height', size)
      .attr('xlink:href', `icons/${name}.svg`)
      .on('click', () => alert(`Sensor clicked!`));

    this.sensors.push({
      id: this.sensorData.properties.id,
      pos: { x: projectedCoords[0], y: projectedCoords[1] },
      type: name,
    });

    this.contextMenuVisible = false;
  }

  onCloseContext() {
    this.contextMenuVisible = false;
  }
}
