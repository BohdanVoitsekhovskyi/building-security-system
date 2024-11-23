import { Component, Input} from '@angular/core';
import { SystemReaction } from '../../../../models/system-reaction.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-system-reaction',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './system-reaction.component.html',
  styleUrl: './system-reaction.component.css'
})
export class SystemReactionComponent {
  @Input({ required: true }) systemReaction!: SystemReaction;

}
