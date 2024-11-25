import { Component, Input } from '@angular/core';
import { SystemReaction } from '../../../../models/system-reaction.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-detailed-system-reaction',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './detailed-system-reaction.component.html',
  styleUrl: './detailed-system-reaction.component.css'
})
export class DetailedSystemReactionComponent {
  @Input({ required: true }) systemReaction!: SystemReaction;
}
