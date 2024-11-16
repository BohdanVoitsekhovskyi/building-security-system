import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PopupService } from '../info-popup/popup.service';
import { PopupInfo } from '../info-popup/popup.model';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  private popupService = inject(PopupService);

  showPopup() {
    const test: PopupInfo = {
      name: 'test',
      description: 'test',
      type: "error"
    };

    this.popupService.showPopup(test);
  }
}
