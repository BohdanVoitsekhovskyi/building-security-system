import { inject, Injectable, OnDestroy, signal } from '@angular/core';
import { from, Observable, Subscription } from 'rxjs';
import { Socket, io } from 'socket.io-client';
import { apiUrl, socketUrl } from '../environment';
import { HttpClient } from '@angular/common/http';
import { SystemReaction } from '../models/system-reaction.model';
import { FacilityService } from './facility.service';
import { FacilityLog } from '../models/facility-log.model';

@Injectable({
  providedIn: 'root',
})
export class TesterService implements OnDestroy {
  private socket: Socket;
  private socketUrl = socketUrl;
  private apiUrl = apiUrl;
  private httpClient = inject(HttpClient);
  private facilityService = inject(FacilityService);

  facilityId = this.facilityService.facilityId;
  systemReaction = signal<SystemReaction[]>([]);
  systemReactionSkipped = signal<SystemReaction[]>([]);
  subscription!: Subscription;
  testStatus: 'stopped' | 'running' | 'not-initiated' = 'not-initiated';
  isRandom: boolean = false;

  constructor() {
    this.socket = io(this.socketUrl, { transports: ['websocket'] });

    this.subscription = this.onLog().subscribe({
      next: (data) => {
        console.log(data);
        this.systemReaction.set([...this.systemReaction(), data]);
        this.systemReactionSkipped.set([
          ...this.systemReactionSkipped().splice(
            this.systemReactionSkipped().findIndex(
              (s) =>
                s.detectorsReaction.at(0)?.detectorReactionTime ===
                data.detectorsReaction.at(0)?.detectorReactionTime,
              1
            )
          ),
        ]);
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
    return this.httpClient.get<FacilityLog>(
      `${this.apiUrl}/test/reactions/${this.facilityId()}`
    );
  }

  exportLog() {
    return this.httpClient.get(
      `${this.apiUrl}/test/reactions/as-file/${this.facilityId()}`,
      { responseType: 'blob' }
    );
  }

  private onLog(): Observable<SystemReaction> {
    return this.on('floorsList');
  }

  stopSimulation() {
    if (this.testStatus === 'running') {
      this.emit('stop-resume-testing', {
        id: this.facilityId(),
        command: 'STOP',
        isRandom: 'false',
      });
      this.testStatus = 'stopped';
    }
  }

  startSimulation(isRandom: boolean) {
    this.isRandom = isRandom;
    if (this.testStatus === 'not-initiated') {
      this.emit('testing-system', {
        id: this.facilityId(),
        command: 'START',
        isRandom: isRandom + '',
      });
    } else if (this.testStatus === 'stopped') {
      this.emit('stop-resume-testing', {
        id: this.facilityId(),
        command: 'RESUME',
        isRandom: isRandom + '',
      });
    } else {
      return;
    }
    this.testStatus = 'running';
  }

  ngOnDestroy() {
    this.stopSimulation();
  }
}
