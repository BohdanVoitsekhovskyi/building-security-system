import { computed, inject, Injectable, signal } from '@angular/core';
import { Floor } from '../models/floor.model';
import { Facility } from '../models/facility.model';
import { HttpClient } from '@angular/common/http';
import { apiUrl } from '../environment';
import { Detector } from '../models/detector.model';
import { AuthService } from './auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class FacilityService {
  private authService = inject(AuthService);
  private httpClient = inject(HttpClient);

  readonly sensorsTypes: { name: string; type: string }[] = [
    { name: 'motion', type: 'entrance' },
    { name: 'motion', type: 'window' },
    { name: 'smoke', type: 'area' },
    { name: 'temperature', type: 'area' },
    { name: 'flood', type: 'area' },
  ];
  facility = signal<Facility | null>(null);
  environment = apiUrl;
  facilityId = computed(() => this.authService.userInfo()?.id);

  constructor() {
    this.getFacility();
  }

  getFacility() {
    this.httpClient
      .get<Facility>(apiUrl + '/facility/' + this.facilityId())
      .subscribe({
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
    return this.httpClient.put<Facility>(
      `${apiUrl}/facility/${this.facilityId()}/floor/${floorNumber}/create`,
      file
    );
  }

  addDetectors(floorNumber: number, detectors: Detector[]) {
    return this.httpClient.put<Facility>(
      `${apiUrl}/facility/${this.facilityId()}/floor/${floorNumber}/edit`,
      detectors
    );
  }

  deleteFloor(floorId: number) {
    return this.httpClient.delete<Facility>(`${apiUrl}/facility/${this.facilityId()}/floor/${floorId}/delete`);
  }

  deleteDetector(floorId: number, detector: Detector) {
    return this.httpClient.delete<Facility>(`${apiUrl}/facility/${this.facilityId()}/floor/${floorId}/delete`);
  }
}
