import { Component, computed, inject, Input } from '@angular/core';
import { SystemReaction } from '../../../../models/system-reaction.model';
import { CommonModule } from '@angular/common';
import { FacilityService } from '../../../../services/facility.service';
import { DetectorReaction } from '../../../../models/detector-reaction.model';

@Component({
  selector: 'app-detailed-system-reaction',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './detailed-system-reaction.component.html',
  styleUrl: './detailed-system-reaction.component.css'
})
export class DetailedSystemReactionComponent {
  @Input({ required: true }) systemReaction!: SystemReaction;
  private facilityService = inject(FacilityService);

  getFloorNumber(reaction: DetectorReaction) {
    const floor =  this.facilityService.facility()?.floors.find((floor) => 
     floor.detectors.some(d => d.id === reaction.detector.id )
    )
    return floor?.floorNumber
  }
}
