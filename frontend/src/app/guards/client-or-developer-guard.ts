import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';

export const clientOrDeveloperGuard: CanActivateFn = (route, state) => {
  
  const authService = inject(AuthService)
  const router = inject(Router)

  if(!authService.isLoggedIn()){
    return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } })
  }

  return authService.hasRole('ROLE_CLIENT') || authService.hasRole('ROLE_DEVELOPER') ? true : router.createUrlTree(['/home'])
};
