import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-timer',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './timer.component.html',
  styleUrl: './timer.component.css',
})
export class TimerComponent {
  @Input() duration: number = 15000;
  @Output() timerComplete = new EventEmitter<void>();

  public animationStyle: any = {};

  ngOnInit(): void {
    this.startTimer();
  }

  startTimer(): void {
    setTimeout(() => {
      this.animationStyle = {
        animation: `progressAnimation ${this.duration}ms linear forwards`,
      };
    }, 50);

    setTimeout(() => {
      this.timerComplete.emit();
    }, this.duration);
  }
}
