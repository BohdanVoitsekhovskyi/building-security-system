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
  path = '/user';

  isLoggedIn = signal<boolean>(false);
  userInfo?: User;

  getUserInfo() {
    return this.userInfo;
  }

  login(userDetails: LoginInfo): Observable<boolean> {
    return this.httpClient
      .post<any>(this.environment + this.path + '/login', userDetails)
      .pipe(
        map((response) => {
          this.userInfo = response;
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
      .post<any>(this.environment + this.path + '/signup', userDetails)
      .pipe(
        map((response) => {
          this.userInfo = response;
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
    this.userInfo = undefined;
    this.isLoggedIn.set(false);
  }
}
