import { Component, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { LoginInfo } from './login.model';
import { AuthService } from '../../../services/auth/auth.service';
import { LoadSpinnerComponent } from "../../../shared/load-spinner/load-spinner.component";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink, LoadSpinnerComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  isLoading: boolean = false;

  loginInfo: LoginInfo = {
    email: '',
    password: '',
  };

  onSubmit(form: NgForm) {
    if (form.invalid) return;

    this.isLoading = true;
    this.authService.login(this.loginInfo).subscribe({
      next: (res) => {
        console.log(res);
        this.isLoading = false;
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.log(err);
        this.isLoading = false;
      }
    });
  }
}
