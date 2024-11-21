import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService, UserSession } from './auth.service';

export const AuthGuard: CanActivateFn = (route, state) => {
  let authService = inject(AuthService);
  let router = inject(Router);
  if (authService.isLoggedIn()) {
    return true;
  } else {
    if (authService.restoreSession()) {
      return true;
    }
    
    router.navigate(['/login']);
    return false;
  }
};
