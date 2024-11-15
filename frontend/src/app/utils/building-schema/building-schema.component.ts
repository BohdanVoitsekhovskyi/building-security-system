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
    const d3Projection = d3Identity.fitSize(
      [this.canvas.w, this.canvas.h],
      geoData['apartment']
    );
    const d3Path = d3.geoPath(d3Projection);

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
    
    const coords: [number, number] = d3.pointer(event);

    console.log(data);
    let leftPoint = data.geometry.coordinates[0][0];
    let rightPoint = data.geometry.coordinates[0][2];
    // Відображаємо новий датчик
    d3.select('.sensors')
      .append('circle')
      .attr('fill', '#000000')
      .attr('cx', (leftPoint[0]+rightPoint[0])/2)
      .attr('cy', (leftPoint[1]+rightPoint[1])/2)
      .attr('r', 8)
      .attr('stroke', 'black')
      .attr('stroke-width', 1)
      .on('click', () => alert(`Sensor clicked!`));

    this.sensors.push({ id: data.properties.id, type: data.properties.type });
  }
}
