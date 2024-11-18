import { Component } from '@angular/core';
import { logs } from './dummy-data';

@Component({
  selector: 'app-logger',
  standalone: true,
  imports: [],
  templateUrl: './logger.component.html',
  styleUrl: './logger.component.css'
})
export class LoggerComponent {
  logs = logs;
}
