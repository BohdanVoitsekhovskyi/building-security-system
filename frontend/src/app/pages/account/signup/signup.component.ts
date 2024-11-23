import { Component, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { SignUpInfo } from './signup.model';
import { AuthService } from '../../../services/auth/auth.service';
import { LoadSpinnerComponent } from '../../../shared/load-spinner/load-spinner.component';
import { debounceTime, Subject } from 'rxjs';

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

  firstnameSubject = new Subject<void>();
  lastnameSubject = new Subject<void>();
  emailSubject = new Subject<void>();
  passwordSubject = new Subject<void>();

  firstnameTouched = false;
  lastnameTouched = false;
  emailTouched = false;
  passwordTouched = false;

  constructor() {
    this.firstnameSubject.pipe(debounceTime(800)).subscribe(() => {
      this.firstnameTouched = true;
    });

    this.lastnameSubject.pipe(debounceTime(800)).subscribe(() => {
      this.passwordTouched = true;
    });

    this.emailSubject.pipe(debounceTime(800)).subscribe(() => {
      this.emailTouched = true;
    });

    this.passwordSubject.pipe(debounceTime(800)).subscribe(() => {
      this.passwordTouched = true;
    });
  }

  onfirstnameInput() {
    this.firstnameTouched = false;
    this.firstnameSubject.next();
  }

  onlastnameInput() {
    this.lastnameTouched = false;
    this.lastnameSubject.next();
  } 
  
  onEmailInput() {
    this.emailTouched = false;
    this.emailSubject.next();
  }

  onPasswordInput() {
    this.passwordTouched = false;
    this.passwordSubject.next();
  }

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
