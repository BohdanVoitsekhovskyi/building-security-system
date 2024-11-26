import {
  Component,
  computed,
  effect,
  inject,
  OnDestroy,
  Signal,
} from '@angular/core';
import { SystemReactionComponent } from '../../pages/main/test/system-reaction/system-reaction.component';
import { SystemReaction } from '../../models/system-reaction.model';
import { FacilityService } from '../../services/facility.service';
import { TesterService } from '../../services/tester.service';
import { Observable, Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';
//import { logs } from './dummy-data';

@Component({
  selector: 'app-logger',
  standalone: true,
  imports: [SystemReactionComponent, FormsModule],
  templateUrl: './logger.component.html',
  styleUrl: './logger.component.css',
})
export class LoggerComponent implements OnDestroy {
  private testerService = inject(TesterService);
  systemReactions = this.testerService.systemReaction;
  subscription?: Subscription;
  isRandom: boolean = false;

  onStart() {
    this.testerService.startSimulation(this.isRandom);
  }

  onStop() {
    this.testerService.stopSimulation();
  }

  markAll() {
    this.testerService.systemReactionSkipped.set(this.systemReactions());
  }

  ngOnDestroy(): void {
    console.log('unsubscribed');
    this.subscription?.unsubscribe();
  }
}
