import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { inject } from '@angular/core';

export const loginGuard: CanActivateFn = (route, state) => {
  
  const authService = inject(AuthService)
  const router = inject(Router)

  if(authService.isLoggedIn()){
    if(confirm('Desea cerrar sesion?')){
      authService.logout()
      return true
    }else{
      return router.createUrlTree(['/home'])
    }
  }

  return true
};
