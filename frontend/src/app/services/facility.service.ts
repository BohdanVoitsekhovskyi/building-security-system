import { inject, Injectable, signal } from '@angular/core';
import { Floor } from '../models/floor.model';
import { Facility } from '../models/facility.model';
import { HttpClient } from '@angular/common/http';
import { apiUrl } from '../environment';

@Injectable({
  providedIn: 'root',
})
export class FacilityService {
  private httpClient = inject(HttpClient);

  readonly sensorsTypes: { name: string; type: string }[] = [
    { name: 'motion', type: 'entrance' },
    { name: 'motion', type: 'window' },
    { name: 'smoke', type: 'area' },
    { name: 'temperature', type: 'area' },
    { name: 'flood', type: 'area' },
  ];
  userHasFacility?: false;
  facility = signal<Facility | null>(null);
  environment = apiUrl;

  loadFacility() {
    //return this.httpClient.get()
  }

  constructor() {
    // this.facility = {
    //   id: 1,
    //   floors: [],
    // };

    // this.getJsonData('example/testbuilding.json');
    // this.getJsonData('example/one_bedroom.json');
    this.getFacility();
  }

  getFacility() {
    this.httpClient.get<Facility>(apiUrl + '/1731850163126').subscribe({
      next: (data) => {
        console.log(data);
        this.facility.set(data);
        // this.getJsonData('example/testbuilding.json');
      },
    });
  }

  // getJsonData(path: string) {
  //   this.httpClient.get<any>(path).subscribe((data) => {
  //     const floor1: Floor = {
  //       id: this.facility.floors.length + 1,
  //       floorNumber: this.facility.floors.length + 1,
  //       placement: data,
  //       detectors: [],
  //     };
  //     this.facility.floors.push(floor1);
  //   });
  // }
}
