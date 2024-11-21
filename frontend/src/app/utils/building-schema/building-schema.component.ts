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
import { Detector } from '../../models/detector.model';
import { PopupService } from '../info-popup/popup.service';

@Component({
  selector: 'app-building-schema',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './building-schema.component.html',
  styleUrl: './building-schema.component.css',
})
export class BuildingSchemaComponent {
  private popupService = inject(PopupService);
  private el = inject(ElementRef);
  private facilityService = inject(FacilityService);

  floor = input.required<Floor>();
  @Input({ required: true }) mode!: 'edit' | 'view';
  @Output() onAddSensor = new EventEmitter<Detector[]>();
  sensors: Detector[] = [];
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
        if (d.properties.type === 'apartment' || this.mode === 'view') return;
        return this.showContextSensor(e, d);
      });

    for (let i of this.floor().detectors) {
      this.addSaveSensor(i.type.toString(), i.position, i.id);
    }
  }

  showContextSensor(event: any, data: any) {
    if (
      this.sensors.find((s) => s.id === data.properties.id) &&
      data.properties.type !== 'area'
    )
      return;

    if (
      d3
        .select('.sensor')
        .selectAll('.area')
        .filter(function () {
          const image: any = d3.select(this);
          return image.attr('id') == data.properties.id;
        })
        .size() >= this.sensorTypes.filter((t) => t.type === 'area').length
    )
      return;

    this.contextMenuVisible = true;
    this.contextMenuPos = { x: event.x, y: event.y };
    this.sensorData = data;
  }

  addSaveSensor(name: string, coords: { x: number; y: number }, id: number) {
    const size = 50;
    const type = this.sensorTypes.find((t) => t.name === name)?.type;
    d3.select('.sensor')
      .append('image')
      .attr('x', coords.x - size / 2)
      .attr('y', coords.y - size / 2)
      .attr('width', size)
      .attr('height', size)
      .attr('xlink:href', `icons/${name.toLowerCase()}.svg`)
      .attr('type', name)
      .attr('class', type || 'default')
      .attr('id', id)
      .on('click', (event) => {
        this.deleteSensor(id, name);
        d3.select(event.target).remove();
      });

    if (type === 'area') {
      let centered = false;

      const count = d3
        .select('.sensor')
        .selectAll('.area')
        .filter(function () {
          const image: any = d3.select(this);
          return image.attr('id') == id;
        })
        .size();

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
            currentX === coords.x - size / 2 &&
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
          } else if (currentX > coords.x - size / 2) {
            newX = currentX + size / 2 + 10;
          } else if (currentX < coords.x - size / 2) {
            newX = currentX - size / 2 - 10;
          } else {
            return;
          }

          image.transition().duration(200).attr('x', newX).attr('y', currentY);
        });
    }

    this.sensors.push({
      id: id,
      position: { x: coords.x, y: coords.y },
      type: name,
    });
  }

  addSensor(event: Event) {
    const name = (event.target as HTMLDivElement).id;
    const coords: [number, number] = d3.polygonCentroid(
      this.sensorData.geometry.coordinates[0]
    );
    const id = this.sensorData.properties.id;
    const projectedCoords = this.projection(coords);

    const size = 50;
    d3.select('.sensor')
      .append('image')
      .attr('x', projectedCoords[0] - size / 2)
      .attr('y', projectedCoords[1] - size / 2)
      .attr('width', size)
      .attr('height', size)
      .attr('xlink:href', `icons/${name.toLowerCase()}.svg`)
      .attr('type', name)
      .attr('class', this.sensorData.properties.type)
      .attr('id', this.sensorData.properties.id)
      .on('click', (event) => {
        this.deleteSensor(this.sensorData.properties.id, name);
        d3.select(event.target).remove();
      });

    if (this.sensorData.properties.type === 'area') {
      let centered = false;

      const count = d3
        .select('.sensor')
        .selectAll('.area')
        .filter(function () {
          const image: any = d3.select(this);
          return image.attr('id') == id;
        })
        .size();

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
      position: { x: projectedCoords[0], y: projectedCoords[1] },
      type: name,
    });

    this.onAddSensor.emit(this.sensors);

    this.contextMenuVisible = false;
  }

  onCloseContext() {
    this.contextMenuVisible = false;
  }

  roomContainsType(id: number, type: string) {
    return this.sensors.findIndex((s) => s.id === id && s.type === type) === -1
      ? false
      : true;
  }

  deleteSensor(id: number, type: string) {
    if (this.mode === 'view') return;
    const sensor = this.sensors.find((s) => s.id === id && s.type === type);
    if (!sensor) {
      console.error('No such sensor');
      return;
    }
    this.facilityService
      .deleteDetector(this.floor().floorNumber, sensor)
      .subscribe({
        next: (res) => {
          this.facilityService.facility.set(res);
          this.popupService.showPopup({
            name: 'Success',
            description: 'Detector was successfully deleted',
            type: 'success',
          });
          this.sensors.splice(
            this.sensors.findIndex(
              (s) => JSON.stringify(s) === JSON.stringify(sensor)
            ),
            1
          );
          console.log(res);
        },
        error: (err) => {
          {
            console.log(err);
            this.popupService.showPopup({
              name: 'Fail',
              description: 'Something went wrong',
              type: 'error',
            });
          }
        },
      });
  }
}
