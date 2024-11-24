import { Component, computed, inject, Signal} from '@angular/core';
import { SystemReactionComponent } from "../../pages/main/test/system-reaction/system-reaction.component";
import { SystemReaction } from '../../models/system-reaction.model';
import { FacilityService } from '../../services/facility.service';
//import { logs } from './dummy-data';

@Component({
  selector: 'app-logger',
  standalone: true,
  imports: [SystemReactionComponent],
  templateUrl: './logger.component.html',
  styleUrl: './logger.component.css'
})
export class LoggerComponent {
  private facilityService = inject(FacilityService);
  private detectors = computed(() => this.facilityService.facility()!.floors[0].detectors);
  reaction: Signal<SystemReaction> = computed<SystemReaction>(() => {
    const reaction = {
      detectors: this.detectors(),
      systemAnswer: "bla bla bla",
      time: new Date() 
    }
    return reaction;
  }); 
}
