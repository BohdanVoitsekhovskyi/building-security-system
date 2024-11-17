import {
  Component,
  ElementRef,
  EventEmitter,
  inject,
  input,
  Input,
  Output,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import * as d3 from 'd3';
import * as topojson from 'topojson-client';
import { CommonModule } from '@angular/common';
import { FacilityService } from '../../services/facility.service';
import { Floor } from '../../models/floor.model';

@Component({
  selector: 'app-building-schema',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './building-schema.component.html',
  styleUrl: './building-schema.component.css',
})
export class BuildingSchemaComponent {
  private el = inject(ElementRef);
  private facilityService = inject(FacilityService);

  floor = input.required<Floor>();
  @Output() onAddSensor = new EventEmitter();
  sensors: { id: number; pos: { x: number; y: number }; type: string }[] = [];
  projection: any;
  private readonly canvas = { w: 1000, h: 1000 };

  sensorTypes = this.facilityService.sensorsTypes;
  contextMenuVisible = false;
  contextMenuPos?: { x: number; y: number };
  sensorData?: any;

  ngOnInit(): void {
    this.renderMap(this.floor().placement);
  }

  private renderMap(topoData: any): void {
    const geoData: Record<string, any> = {};
    const arrOfKeys = Object.keys(topoData.objects);

    arrOfKeys.forEach((key) => {
      geoData[key] = topojson.feature(topoData, topoData.objects[key]);
    });

    arrOfKeys.push('sensor');

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

    const renderOrder = ['apartment', 'area', 'window', 'entrance', 'sensor'];

    const groups = svgContainer
      .selectAll('g')
      .data(renderOrder)
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
        if (d.properties.type === 'apartment') return;
        return this.showContextSensor(e, d);
      });
  }

  showContextSensor(event: any, data: any) {
    if (
      this.sensors.find((s) => s.id === data.properties.id) &&
      data.properties.type !== 'area'
    )
      return;

    this.contextMenuVisible = true;
    this.contextMenuPos = { x: event.x, y: event.y };
    this.sensorData = data;
  }

  addSensor(event: Event) {
    const name = (event.target as HTMLDivElement).id;
    console.log(name);
    const coords: [number, number] = d3.polygonCentroid(
      this.sensorData.geometry.coordinates[0]
    );

    const projectedCoords = this.projection(coords);

    const size = 50;

    d3.select('.sensor')
      .append('image')
      .attr('x', projectedCoords[0] - size / 2)
      .attr('y', projectedCoords[1] - size / 2)
      .attr('width', size)
      .attr('height', size)
      .attr('xlink:href', `icons/${name}.svg`)
      .attr('class', this.sensorData.properties.type)
      .attr('id', this.sensorData.properties.id)
      .on('click', () => alert(`Sensor clicked!`));

    if (this.sensorData.properties.type === 'area') {
      let centered = false;
      const id = this.sensorData.properties.id;

      const count = d3
        .select('.sensor')
        .selectAll('.area')
        .filter(function () {
          const image: any = d3.select(this);
          return image.attr('id') == id;
        })
        .size();
      console.log(this.sensorData);
      d3.select('.sensor')
        .selectAll('.area')
        .each(function () {
          const image: any = d3.select(this);
          const currentX = parseFloat(image.attr('x')) || 0;
          const currentY = parseFloat(image.attr('y')) || 0;

          if (count == 0) {
            return;
          }

          if (image.attr('id') != id) return;

          let newX = 0;
          if (
            currentX === projectedCoords[0] - size / 2 &&
            count > 1 &&
            count % 2 === 0
          ) {
            if (centered === false) {
              newX = currentX - size / 2 - 10;
              centered = true;
            } else {
              newX = currentX + size / 2 + 10;
              centered = false;
            }
          } else if (currentX > projectedCoords[0] - size / 2) {
            newX = currentX + size / 2 + 10;
          } else if (currentX < projectedCoords[0] - size / 2) {
            newX = currentX - size / 2 - 10;
          } else {
            return;
          }

          image.transition().duration(200).attr('x', newX).attr('y', currentY);
        });
    }

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
