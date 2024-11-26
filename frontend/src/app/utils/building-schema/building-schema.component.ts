import {
  Component,
  effect,
  EffectRef,
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
import { TesterService } from '../../services/tester.service';
import { Subscription } from 'rxjs';

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
  private testerService = inject(TesterService);

  floor = input.required<Floor>();
  @Input({ required: true }) mode!: 'edit' | 'view';
  @Output() onChangeDetectors = new EventEmitter<Detector[]>();
  detectors: Detector[] = [];

  projection: any;
  private readonly canvas = { w: 1000, h: 1000 };

  detectorTypes = this.facilityService.detectorTypes;
  contextMenuVisible = false;
  contextMenuPos?: { x: number; y: number };

  currentDetector?: Detector;
  detectorSize = 50;

  systemReactions = this.testerService.systemReaction;
  subscription!: EffectRef;

  constructor() {
    this.subscription = effect(() => {
      this.systemReactions().forEach((dr) => {
        dr.detectorsReaction.forEach((dr) => {
          d3.select(`image[id="${dr.detector.id}"]`).attr('class', 'alert');
        });
      });
      this.testerService.systemReactionSkipped().forEach((dr) => {
        dr.detectorsReaction.forEach((dr) => {
          d3.select(`image[id="${dr.detector.id}"]`).attr('class', '');
        });
      });
    });
  }

  ngOnInit(): void {
    this.renderMap(this.floor().placement);
    if (this.mode !== 'view') {
      this.subscription.destroy();
      return;
    }

    if (this.mode === 'view') {
      this.systemReactions().forEach((dr) => {
        dr.detectorsReaction.forEach((dr) => {
          d3.select(`image[id="${dr.detector.id}"]`).attr('class', 'alert');
        });
      });

      this.testerService.systemReactionSkipped().forEach((dr) => {
        dr.detectorsReaction.forEach((dr) => {
          d3.select(`image[id="${dr.detector.id}"]`).attr('class', '');
        });
      });
    }
  }

  private renderMap(topoData: any): void {
    const geoData: Record<string, any> = {};
    const arrOfKeys = Object.keys(topoData.objects);

    arrOfKeys.forEach((key) => {
      geoData[key] = topojson.feature(topoData, topoData.objects[key]);
    });

    arrOfKeys.push('detector');
    const renderOrder = ['apartment', 'area', 'window', 'entrance', 'detector'];

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
      .attr('id', (d: any) => d.properties.id)
      .on('click', (e: any, d: any) => {
        if (this.mode === 'view' || d.properties.type === 'apartment') return;
        return this.showContextDetector(e, d);
      });

    for (let detector of this.floor().detectors) {
      detector.type.toLowerCase();
      this.addDetector(detector);
    }
  }

  containsDetector(furnitureId: number, type: string) {
    return (
      d3
        .select('.detector')
        .selectAll('image')
        .filter(function () {
          const detector: any = d3.select(this);
          return (
            detector.attr('type') == type && detector.attr('fid') == furnitureId
          );
        })
        .size() > 0
    );
  }

  showContextDetector(event: any, data: any) {
    if (
      this.detectors.find((s) => s.furnitureId === data.properties.id) &&
      data.properties.type !== 'area'
    )
      return;

    const coords: [number, number] = d3.polygonCentroid(
      data.geometry.coordinates[0]
    );
    const projectedCoords = this.projection(coords);

    this.currentDetector = {
      id: 0,
      furnitureId: data.properties.id,
      position: {
        x: projectedCoords[0] - this.detectorSize / 2,
        y: projectedCoords[1] - this.detectorSize / 2,
      },
      type: '',
    };
    this.contextMenuVisible = true;
    this.contextMenuPos = { x: event.x, y: event.y };
    this.detectorTypes = this.facilityService.detectorTypes.filter(
      (t) =>
        t.facilityType === data.properties.type &&
        !this.containsDetector(data.properties.id, t.type)
    );
  }

  onAddDetector(event: Event) {
    if (!this.currentDetector) {
      console.error('No detector chosen');
      return;
    }
    const type = (event.target as HTMLDivElement).id;
    this.currentDetector.type = type;
    this.addDetector(this.currentDetector!, 500);
    this.onChangeDetectors.emit(this.detectors);
    this.contextMenuVisible = false;
  }

  addDetector(detector: Detector, animation?: number) {
    d3.select('.detector')
      .append('image')
      .attr('x', detector.position.x)
      .attr('y', detector.position.y)
      .attr('width', this.detectorSize)
      .attr('height', this.detectorSize)
      .attr('xlink:href', `icons/${detector.type.toLowerCase()}.svg`)
      .attr('type', detector.type.toLowerCase())
      .attr('id', detector.id)
      .attr('fid', detector.furnitureId)
      .on('click', (event) => {
        if (this.mode === 'view') return;
        this.detectors.splice(
          this.detectors.findIndex((d) => detector.id === d.id),
          1
        );
        this.onChangeDetectors.emit(this.detectors);
        d3.select(event.target).remove();
        this.placeDetectors(detector.furnitureId, 500);
      });

    this.placeDetectors(detector.furnitureId, animation);

    this.detectors.push(detector);
  }

  placeDetectors(furnitureId: number, animation?: number) {
    let count = d3
      .select('.detector')
      .selectAll('image')
      .filter(function () {
        const image: any = d3.select(this);
        return image.attr('fid') == furnitureId;
      })
      .size();

    const area: any = d3
      .select('.area')
      .selectAll('path')
      .filter(function () {
        const path: any = d3.select(this);
        return path.attr('id') == furnitureId;
      })
      .node();

    if (!area) return;

    let centroid: { x: number; y: number };
    if (area) {
      const pathElement = area as SVGPathElement;

      const length = pathElement.getTotalLength();

      let sumX = 0,
        sumY = 0,
        numPoints = 100;
      for (let i = 0; i <= numPoints; i++) {
        const point = pathElement.getPointAtLength((i / numPoints) * length);
        sumX += point.x;
        sumY += point.y;
      }

      centroid = { x: sumX / numPoints, y: sumY / numPoints };
    }

    count = count / 2 - 1;

    d3.select('.detector')
      .selectAll('image')
      .nodes()
      .forEach((node: any) => {
        const image: any = d3.select(node);
        if (image.attr('fid') != furnitureId) return;

        const newX = centroid.x + this.detectorSize * count + 10 * count;
        count--;

        const currentY = parseFloat(image.attr('y')) || 0;

        if (animation) {
          image
            .transition()
            .duration(animation)
            .attr('x', newX)
            .attr('y', currentY);
        } else {
          image.attr('x', newX).attr('y', currentY);
        }
      });
  }

  onCloseContext() {
    this.contextMenuVisible = false;
  }
}
