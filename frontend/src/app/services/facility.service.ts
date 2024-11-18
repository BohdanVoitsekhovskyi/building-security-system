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
  facilityId = 1731850163126;

  constructor() {
    this.getFacility();
  }

  getFacility() {
    this.httpClient.get<Facility>(apiUrl + '/' + this.facilityId).subscribe({
      next: (data) => {
        console.log(data);
        this.facility.set(data);
      },
    });
  }

  addFloor(floorNumber: number, file: string) {
    return this.httpClient.post<Facility>(
      `${apiUrl}/${this.facilityId}/floor/${floorNumber}/create`,
      file
    );
  }
}
