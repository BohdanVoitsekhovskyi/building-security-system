import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css',
})
export class UserProfileComponent {
  private router = inject(Router);
  private authService = inject(AuthService);

  userInfo = this.authService.userInfo;

  onLogout() {
    this.authService.logout();
    this.router.navigate(['']);
  }
}
