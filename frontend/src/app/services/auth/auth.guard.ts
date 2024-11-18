import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const AuthGuard: CanActivateFn = (route, state) => {
  let isauthenticated = inject(AuthService).isLoggedIn;
  let router = inject(Router);
  if (isauthenticated()) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};
