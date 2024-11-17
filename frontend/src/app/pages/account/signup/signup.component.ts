import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { SignUpInfo } from './signup.model';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})
export class SignupComponent {
  signInInfo: SignUpInfo = {
    name: '',
    surname: '',
    email: '',
    password: '',
  };

  onSubmit() {
    console.log(this.signInInfo);
  }
}
