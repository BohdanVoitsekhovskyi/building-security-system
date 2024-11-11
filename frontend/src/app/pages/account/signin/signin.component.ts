import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { SignInInfo } from './signin.model';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.css',
})
export class SigninComponent {
  signInInfo: SignInInfo = {
    name: '',
    surname: '',
    email: '',
    password: '',
  };

  //validation

  onSubmit() {}
}
