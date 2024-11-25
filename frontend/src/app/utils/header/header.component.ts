import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PopupService } from '../info-popup/popup.service';
import { PopupInfo } from '../info-popup/popup.model';
import { AuthService } from '../../services/auth/auth.service';
import { UserProfileComponent } from "../../shared/user-profile/user-profile.component";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, UserProfileComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  private authService = inject(AuthService);
  isAuthenticated = this.authService.isLoggedIn;

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
