import { inject, Injectable, signal } from '@angular/core';
import { from, Observable } from 'rxjs';
import { Socket, io } from 'socket.io-client';
import { apiUrl, socketUrl } from '../environment';
import { HttpClient } from '@angular/common/http';
import { SystemReaction } from '../models/system-reaction.model';
import { FacilityService } from './facility.service';
import { Detector } from '../models/detector.model';

@Injectable({
  providedIn: 'root',
})
export class TesterService {
  private socket: Socket;
  private socketUrl = socketUrl;
  private apiUrl = apiUrl;
  private httpClient = inject(HttpClient);
  private facilityService = inject(FacilityService);

  facilityId = this.facilityService.facilityId();

  constructor() {
    this.socket = io(this.socketUrl, { transports: ['websocket'] });
  }

  private emit(event: string, data: any) {
    this.socket.emit(event, data);

    this.socket.on('connect_error', () => {
      console.log('error');
    });
  }

  private on(event: string): Observable<any> {
    return new Observable((observer) => {
      this.socket.on(event, (data) => {
        observer.next(data);
      });

      return () => {
        this.socket.off(event);
      };
    });
  }

  getLogs() {
    return this.httpClient.get<SystemReaction[]>(
      `${this.apiUrl}/logs/${this.facilityId}`
    );
    //TODO
  }

  onLog(): Observable<SystemReaction[]> {
    return this.on('floorsList');
  }

  stopSimulation() {
    this.emit('stop-resume-testing', {
      contents: `STOP:${this.facilityId}`,
    });
  }

  startSimulation() {
    this.emit('testing-system', {
      contents: this.facilityId,
    });
  }
}
