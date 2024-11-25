import { Component, inject } from '@angular/core';
import { NavbarComponent } from "../../../utils/navbar/navbar.component";
import { TesterService } from '../../../services/tester.service';
import { SystemReaction } from '../../../models/system-reaction.model';
import { Subscription } from 'rxjs';
import { DetailedSystemReactionComponent } from "./detailed-system-reaction/detailed-system-reaction.component";

@Component({
  selector: 'app-log',
  standalone: true,
  imports: [NavbarComponent, DetailedSystemReactionComponent],
  templateUrl: './log.component.html',
  styleUrl: './log.component.css'
})
export class LogComponent {
  private testerService = inject(TesterService);
  systemReactions: SystemReaction[] = [];

  socket?: Subscription;

  constructor() {
    this.socket = this.testerService.onLog().subscribe({
      next: (data) => {
        console.log(data);
        this.systemReactions = [...this.systemReactions, data];
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  ngOnDestroy(): void {
    this.socket?.unsubscribe();
  }

  onExport() {
    //TODO
  }
}
