import { Component, computed, inject, Signal } from '@angular/core';
import { NavbarComponent } from '../../../utils/navbar/navbar.component';
import { TesterService } from '../../../services/tester.service';
import { SystemReaction } from '../../../models/system-reaction.model';
import { Subscription } from 'rxjs';
import { DetailedSystemReactionComponent } from './detailed-system-reaction/detailed-system-reaction.component';
import { LoadSpinnerAltComponent } from "../../../shared/load-spinner-alt/load-spinner-alt.component";

@Component({
  selector: 'app-log',
  standalone: true,
  imports: [NavbarComponent, DetailedSystemReactionComponent, LoadSpinnerAltComponent],
  templateUrl: './log.component.html',
  styleUrl: './log.component.css',
})
export class LogComponent {
  private testerService = inject(TesterService);
  systemReactions?: Signal<SystemReaction[]>;
  private subscription!: Subscription;

  ngOnInit() {
    this.subscription = this.testerService.getLogs().subscribe({
      next: (data) => {
        console.log(data);
        this.systemReactions = computed(() => [...data.logMessages]);
      },
    });
  }

  onExport() {
    this.testerService.exportLog().subscribe((blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'log.txt';
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
