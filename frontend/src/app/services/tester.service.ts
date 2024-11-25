import { inject, Injectable } from '@angular/core';
import { from, Observable } from 'rxjs';
import { Socket, io } from 'socket.io-client';
import { apiUrl, socketUrl } from '../environment';
import { HttpClient } from '@angular/common/http';
import { SystemReaction } from '../models/system-reaction.model';
import { FacilityService } from './facility.service';

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
    this.socket = io(this.socketUrl, {
      transports: ['websocket'], // Force WebSocket transport
    });
  }

  private emit(event: string, data: any) {
    this.socket.emit(event, data);
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

  onLog(): Observable<any> {
    return this.on('testing-system');
    //TODO
  }

  stopSimulation() {
    this.emit('stop', { message: 'stop' });
    //TODO
  }

  startSimulation() {
    this.emit('start', { message: this.facilityId });
    //TODO
  }

  //temp functions
  testRequest() {
    this.emit('testing-system', this.facilityId);
  }

  getAnswer(): Observable<SystemReaction> {
    return this.on('testing-result');
  }
}
