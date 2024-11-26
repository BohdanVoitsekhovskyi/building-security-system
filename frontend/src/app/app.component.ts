import { Component, computed, inject } from '@angular/core';
import { HeaderComponent } from './utils/header/header.component';
import { RouterOutlet } from '@angular/router';
import { InfoPopupComponent } from './utils/info-popup/info-popup.component';
import { PopupService } from './utils/info-popup/popup.service';
import { CommonModule } from '@angular/common';
import { TesterService } from './services/tester.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HeaderComponent, RouterOutlet, InfoPopupComponent, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'frontend';

  private testerService = inject(TesterService);
  private popupService = inject(PopupService);
  popupVisible = this.popupService.popupIsVisible;
  popupType = computed(() => this.popupService.popupInfo()?.type);
}
