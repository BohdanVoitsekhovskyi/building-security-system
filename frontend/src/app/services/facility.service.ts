import { inject, Injectable, signal } from '@angular/core';
import { Floor } from '../models/floor.model';
import { Facility } from '../models/facility.model';
import { HttpClient } from '@angular/common/http';
import { apiUrl } from '../environment';
import { Detector } from '../models/detector.model';

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
<<<<<<< HEAD
  facilityId = 1731945034824;
=======
  facilityId = computed(() => this.authService.userInfoSignal()?.id);
>>>>>>> 55e4b68 (Fix frontend)

  constructor() {
    this.getFacility();
  }

  getFacility() {
<<<<<<< HEAD
    this.httpClient.get<Facility>(apiUrl + '/' + this.facilityId).subscribe({
=======
    this.httpClient.get<Facility>(apiUrl + '/facility/' + this.facilityId()).subscribe({
>>>>>>> 55e4b68 (Fix frontend)
      next: (data) => {
        console.log(data);
        this.facility.set(data);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  addFloor(floorNumber: number, file: string) {
<<<<<<< HEAD
    return this.httpClient.post<Facility>(
      `${apiUrl}/${this.facilityId}/floor/${floorNumber}/create`,
=======
    return this.httpClient.put<Facility>(
      `${apiUrl}/facility/${this.facilityId()}/floor/${floorNumber}/create`,
>>>>>>> 55e4b68 (Fix frontend)
      file
    );
  }

  addDetectors(floorNumber: number, detectors: Detector[]) {
    ///
  }
}
