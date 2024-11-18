import { Component, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { SignUpInfo } from './signup.model';
import { AuthService } from '../../../services/auth/auth.service';
import { LoadSpinnerComponent } from '../../../shared/load-spinner/load-spinner.component';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [FormsModule, RouterLink, LoadSpinnerComponent],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})
export class SignupComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  isLoading: boolean = false;

  signInInfo: SignUpInfo = {
    firstname: '',
    lastname: '',
    email: '',
    password: '',
  };

  onSubmit(form: NgForm) {
    if (form.invalid) return;

    this.isLoading = true;
    this.authService.singup(this.signInInfo).subscribe({
      next: (res) => {
        console.log(res);
        this.isLoading = false;
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.log(err);
        this.isLoading = false;
      },
    });
  }
}
