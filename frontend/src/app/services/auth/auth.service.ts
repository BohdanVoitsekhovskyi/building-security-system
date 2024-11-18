import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { apiUrl } from '../../environment';
import { LoginInfo } from '../../pages/account/login/login.model';
import { SignUpInfo } from '../../pages/account/signup/signup.model';
import { User } from '../../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private httpClient = inject(HttpClient);
  environment = apiUrl;

  isLoggedIn = signal<boolean>(false);
  userInfoSignal = signal<User | undefined>(undefined);

  login(userDetails: LoginInfo): Observable<boolean> {
    return this.httpClient
      .post<any>(this.environment + '/login', userDetails)
      .pipe(
        map((response) => {
          this.userInfoSignal.set(response);
          this.isLoggedIn.set(true);
          return true;
        }),
        catchError((error) => {
          console.log(error);
          this.isLoggedIn.set(false);
          return of(false);
        })
      );
  }

  singup(userDetails: SignUpInfo): Observable<boolean> {
    return this.httpClient
      .post<any>(this.environment + '/signup', userDetails)
      .pipe(
        map((response) => {
          this.userInfoSignal = response;
          this.isLoggedIn.set(true);
          return true;
        }),
        catchError((error) => {
          console.log(error);
          this.isLoggedIn.set(false);
          return of(false);
        })
      );
  }

  logout(): void {
    this.userInfoSignal.set(undefined);
    this.isLoggedIn.set(false);
  }
}
