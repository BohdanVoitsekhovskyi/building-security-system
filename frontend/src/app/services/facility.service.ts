import { inject, Injectable } from '@angular/core';
import { Floor } from '../models/floor.model';
import { Facility } from '../models/facility.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FacilityService {
  private httpClient = inject(HttpClient);

  readonly sensorsTypes: { name: string, type: string }[] = [
    { name: 'motion', type: 'door' },
    { name: 'motion', type: 'window' },
    { name: 'smoke', type: 'room' },
    { name: 'temperature', type: 'room' },
    { name: 'flood', type: 'room' },
  ];
  userHasFacility?: false;
  activeFloor?: Floor;
  facility!: Facility;

  loadFacility() {
    //return this.httpClient.get()
  }

  constructor() { 
    this.facility = {
      id: 1,
      floors: []
    };

    this.getJsonData('example/testbuilding.json');
    this.getJsonData('example/one_bedroom.json');
  }

  getJsonData(path: string) {
    this.httpClient.get<any>(path).subscribe((data) => {
      const floor1: Floor = {
        id: this.facility.floors.length + 1,
        data: data,
        detectors: []
      };
      this.facility.floors.push(floor1);
    });
  }
}
