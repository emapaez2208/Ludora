import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth-service';

export const authInterceptorInterceptor: HttpInterceptorFn = (req, next) => {

  if (req.url.includes('api.cloudinary.com')) {
    return next(req);
  }
  
  const router = inject(Router);
  const auth = inject(AuthService);
  const token = auth.getToken();

  const newReq = token
    ? req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      })
    : req;

  return next(newReq).pipe(
    catchError((error: HttpErrorResponse) => {
      const isLoginRequest = req.url.includes('/auth/login');

      if (error.status === 401) {
        if (!isLoginRequest) {
          console.error('No autorizado - Cerrando sesión...');
          auth.logout();
          router.navigate(['/login']);
        }
      } else if (error.status === 403) {
        if (!isLoginRequest) {
          console.error('No autorizado - ', error)
          router.navigate(['/home'])
        }else{
          console.error('Acceso denegado - Cuenta blockeada');
        }

      }

      return throwError(() => error);
    })
  );
};
