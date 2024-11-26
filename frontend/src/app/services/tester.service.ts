import { inject, Injectable, OnDestroy, signal } from '@angular/core';
import { from, Observable, Subscription } from 'rxjs';
import { Socket, io } from 'socket.io-client';
import { apiUrl, socketUrl } from '../environment';
import { HttpClient } from '@angular/common/http';
import { SystemReaction } from '../models/system-reaction.model';
import { FacilityService } from './facility.service';
import { Detector } from '../models/detector.model';

@Injectable({
  providedIn: 'root',
})
export class TesterService implements OnDestroy {
  private socket: Socket;
  private socketUrl = socketUrl;
  private apiUrl = apiUrl;
  private httpClient = inject(HttpClient);
  private facilityService = inject(FacilityService);

  facilityId = this.facilityService.facilityId();
  systemReaction = signal<SystemReaction[]>([]);
  subscription!: Subscription;
  testStatus: 'stopped' | 'running' | 'not-initiated' = 'not-initiated';

  constructor() {
    this.socket = io(this.socketUrl, { transports: ['websocket'] });

    this.subscription = this.onLog().subscribe({
      next: (data) => {
        this.systemReaction.set([...this.systemReaction(), data]);
      },
      error: (err) => {
        console.error(err);
      },
    });
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

  exportLog() {
    return this.httpClient.get(`${this.apiUrl}/logFile`,{ responseType: 'blob' });  
  }

  private onLog(): Observable<SystemReaction> {
    return this.on('floorsList');
  }

  stopSimulation() {
    if (this.testStatus === 'running') {
      this.emit('stop-resume-testing', {
        contents: `STOP:${this.facilityId}`,
      });
      this.testStatus = 'stopped';
    }
  }

  startSimulation() {
    if (this.testStatus === 'not-initiated') {
      this.emit('testing-system', {
        contents: this.facilityId,
      });
    } else if (this.testStatus === 'stopped') {
      this.emit('stop-resume-testing', {
        contents: `RESUME:${this.facilityId}`,
      });
    } else {
      return;
    }
    this.testStatus = 'running';
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
