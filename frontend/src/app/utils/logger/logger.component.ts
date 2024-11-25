import { Component, computed, inject, OnDestroy, Signal } from '@angular/core';
import { SystemReactionComponent } from '../../pages/main/test/system-reaction/system-reaction.component';
import { SystemReaction } from '../../models/system-reaction.model';
import { FacilityService } from '../../services/facility.service';
import { TesterService } from '../../services/tester.service';
import { Observable, Subscription } from 'rxjs';
//import { logs } from './dummy-data';

@Component({
  selector: 'app-logger',
  standalone: true,
  imports: [SystemReactionComponent],
  templateUrl: './logger.component.html',
  styleUrl: './logger.component.css',
})
export class LoggerComponent implements OnDestroy {
  private facilityService = inject(FacilityService);
  private testerService = inject(TesterService);
  systemReactions: SystemReaction[] = [];

  socket?: Subscription;

  constructor() {
    this.socket = this.testerService
    .getAnswer()
    .subscribe((data) => {
      console.log(data);
      this.systemReactions = [...this.systemReactions, data];
    });
  }

  onStart() {
    this.testerService.testRequest();
  }

  ngOnDestroy(): void {
    this.socket?.unsubscribe();
  }
}
