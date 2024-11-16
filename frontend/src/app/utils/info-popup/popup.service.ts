import { Injectable, signal } from '@angular/core';
import { PopupInfo } from './popup.model';

@Injectable({
  providedIn: 'root',
})
export class PopupService {
  private popupIsVisibleSignal = signal<boolean>(false);
  private popupInfoSignal = signal<PopupInfo | null>(null);
  popupIsVisible = this.popupIsVisibleSignal.asReadonly();
  popupInfo = this.popupInfoSignal.asReadonly();

  showPopup(info: PopupInfo) {
    this.popupIsVisibleSignal.set(true);
    this.popupInfoSignal.set(info);
    setTimeout(() => {
      this.popupIsVisibleSignal.set(false);
    }, 4000);
  }
}
