import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth-service';

export const authInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
  
  const router = inject(Router)
  const auth = inject(AuthService)
  const token = auth.getToken()

  const newReq = token
  ? req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  })
  : req

  return next(newReq).pipe(
    catchError((error : HttpErrorResponse) => {
      if (error.status === 401 && !req.url.includes('/auth/login')){
        return throwError(() => error)
      }else if(error.status === 401){
        console.error('No Autorizado - Redirigiendo...')
        auth.logout()
        router.navigate(['/login'])
      }else if(error.status === 403){
        console.error('No tiene permiso - Redirigiendo...')
        router.navigate(['/home'])
      }

      return throwError(() => error)
    })
  )
};
