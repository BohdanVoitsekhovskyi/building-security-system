import { Component, inject, Input } from '@angular/core';
import { SystemReaction } from '../../../../models/system-reaction.model';
import { CommonModule } from '@angular/common';
import { TesterService } from '../../../../services/tester.service';
import { TimerComponent } from '../../../../shared/timer/timer.component';

@Component({
  selector: 'app-system-reaction',
  standalone: true,
  imports: [CommonModule, TimerComponent],
  templateUrl: './system-reaction.component.html',
  styleUrl: './system-reaction.component.css',
})
export class SystemReactionComponent {
  private testerService = inject(TesterService);
  @Input({ required: true }) systemReaction!: SystemReaction;
  skipped = this.testerService.systemReactionSkipped();

  onDelete() {
    this.testerService.systemReactionSkipped.set([
      ...this.testerService.systemReactionSkipped(),
      this.systemReaction,
    ]);
  }

  isMarked(systemReaction: SystemReaction) {
    return this.skipped.find(
      (s) =>
        s.detectorsReaction.at(0)?.detectorReactionTime ===
        systemReaction.detectorsReaction.at(0)?.detectorReactionTime
    );
  }
}
