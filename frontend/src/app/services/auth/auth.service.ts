import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { apiUrl } from '../../environment';
import { LoginInfo } from '../../pages/account/login/login.model';
import { SignUpInfo } from '../../pages/account/signup/signup.model';
import { User } from '../../models/user.model';
import { FacilityService } from '../facility.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private httpClient = inject(HttpClient);

  environment = apiUrl;
  path = '/user';

  isLoggedIn = signal<boolean>(false);
  userInfo = signal<User | undefined>(undefined);
  

  login(userDetails: LoginInfo): Observable<boolean> {
    return this.httpClient
      .post<any>(this.environment + this.path + '/login', userDetails)
      .pipe(
        map((response) => {
          this.userInfo.set(response);
          this.isLoggedIn.set(true);

          const currentDate = new Date();
          currentDate.setDate(currentDate.getDate() + 1);
          const userSession: UserSession = {
            id: this.userInfo()!.id,
            firstname: this.userInfo()!.firstname,
            lastname: this.userInfo()!.lastname,
            email: this.userInfo()!.email,
            expire: Date.now() + 1000 * 60 * 60 * 3,
          };
          localStorage.setItem('auth', JSON.stringify(userSession));
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
          this.userInfo.set(response);
          this.isLoggedIn.set(true);

          const userSession: UserSession = {
            id: this.userInfo()!.id,
            firstname: this.userInfo()!.firstname,
            lastname: this.userInfo()!.lastname,
            email: this.userInfo()!.email,
            expire: Date.now() + 1000 * 60 * 60 * 3,
          };
          localStorage.setItem('auth', JSON.stringify(userSession));
          return true;
        }),
        catchError((error) => {
          console.log(error);
          this.isLoggedIn.set(false);
          return of(false);
        })
      );
  }

  restoreSession(): boolean {
    const storedAuth = localStorage.getItem('auth');
    if (storedAuth) {
      try {
        const auth: UserSession = JSON.parse(storedAuth);

        if (auth.expire > Date.now()) {
          this.isLoggedIn.set(true);
          this.userInfo.set({
            id: auth.id,
            firstname: auth.firstname,
            lastname: auth.lastname,
            password: '',
            email: auth.email,
          });
          return true;
        }
      } catch (error) {
        console.error('Failed to parse stored auth data:', error);
      }
    }
    this.logout();
    return false;
  }

  logout(): void {
    this.userInfo.set(undefined);
    this.isLoggedIn.set(false);
    localStorage.removeItem('auth');
  }
}

export type UserSession = {
  id: number;
  firstname: string;
  lastname: string;
  email: string;
  expire: number;
};
