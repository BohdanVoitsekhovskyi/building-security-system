import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import * as d3 from 'd3';
import * as topojson from 'topojson-client';

@Component({
  selector: 'app-building-schema',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './building-schema.component.html',
  styleUrl: './building-schema.component.css',
})
export class BuildingSchemaComponent {
  sensors: { type: string; id: string }[] = [];
  projection: any;
  private readonly canvas = { w: 1000, h: 1000 };

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
      .on('click', (e, d) => this.addSensor(e, d));
  }

  addSensor(event: any, data: any) {
    if (
      this.sensors.find(
        (s) => s.id === data.properties.id && s.type === data.properties.type
      )
    )
      return;

    console.log(
      data.geometry.coordinates[0].slice(
        0,
        data.geometry.coordinates[0].length - 1
      )
    );

    console.log(
      d3.polygonCentroid(
        data.geometry.coordinates[0].slice(
          0,
          data.geometry.coordinates[0].length - 1
        )
      )
    );

    const coords: [number, number] = d3.polygonCentroid(
      data.geometry.coordinates[0]
    );

    const projectedCoords = this.projection(coords);

    d3.select('.sensors')
      .append('circle')
      .attr('fill', '#000000')
      .attr('cx', projectedCoords[0])
      .attr('cy', projectedCoords[1])
      .attr('r', 8)
      .attr('stroke', 'black')
      .attr('stroke-width', 1)
      .on('click', () => alert(`Sensor clicked!`));

    this.sensors.push({ id: data.properties.id, type: data.properties.type });
  }
}
