import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { apiUrl } from '../../environment';
import { LoginInfo } from '../../pages/account/login/login.model';
import { SignUpInfo } from '../../pages/account/signup/signup.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private httpClient = inject(HttpClient);
  environment = apiUrl;

  isLoggedIn = signal<boolean>(true);

  login(userDetails: LoginInfo): Observable<boolean> {
    return this.httpClient
      .post<any>(this.environment + '/login', userDetails)
      .pipe(
        map((response) => {
          localStorage.setItem('JWT_Token', response.token);
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
          localStorage.setItem('JWT_Token', response.token);
          this.isLoggedIn.set(false);
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
    localStorage.removeItem('JWT_Token');
  }
}
