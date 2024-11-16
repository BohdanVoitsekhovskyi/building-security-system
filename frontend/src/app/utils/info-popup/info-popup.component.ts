import { Component, computed, effect, HostBinding, inject, signal } from '@angular/core';
import { PopupService } from './popup.service';

@Component({
  selector: 'app-info-popup',
  standalone: true,
  imports: [],
  templateUrl: './info-popup.component.html',
  styleUrl: './info-popup.component.css'
})
export class InfoPopupComponent {
  private popupService = inject(PopupService);
  popupInfo = this.popupService.popupInfo;
}
