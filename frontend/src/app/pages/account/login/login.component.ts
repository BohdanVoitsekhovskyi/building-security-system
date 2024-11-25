import { Component, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { LoginInfo } from './login.model';
import { AuthService } from '../../../services/auth/auth.service';
import { LoadSpinnerComponent } from '../../../shared/load-spinner/load-spinner.component';
import { PopupService } from '../../../utils/info-popup/popup.service';
import { PopupInfo } from '../../../utils/info-popup/popup.model';
import { debounceTime, Subject } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink, LoadSpinnerComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  private popupService = inject(PopupService);
  private authService = inject(AuthService);
  private router = inject(Router);
  isLoading: boolean = false;

  emailSubject = new Subject<void>();
  passwordSubject = new Subject<void>();
  emailTouched = false;
  passwordTouched = false;

  constructor() {
    this.emailSubject.pipe(debounceTime(800)).subscribe(() => {
      this.emailTouched = true;
    });

    this.passwordSubject.pipe(debounceTime(800)).subscribe(() => {
      this.passwordTouched = true;
    });
  }

  onEmailInput() {
    this.emailTouched = false;
    this.emailSubject.next();
  }

  onPasswordInput() {
    this.passwordTouched = false;
    this.passwordSubject.next();
  }

  loginInfo: LoginInfo = {
    email: '',
    password: '',
  };

  onSubmit(form: NgForm) {
    if (form.invalid) return;

    debugger;
    this.isLoading = true;
    this.authService.login(this.loginInfo).subscribe({
      next: (res) => {
        this.isLoading = false;
        if (!res) {
          const test: PopupInfo = {
            name: 'Fail',
            description: 'Wrong password or email',
            type: 'error',
          };
          this.popupService.showPopup(test);
        } else {
          this.router.navigate(['/home']);
        }
      },
      error: (err) => {
        console.log(err);
        this.isLoading = false;
      },
    });
  }
}
