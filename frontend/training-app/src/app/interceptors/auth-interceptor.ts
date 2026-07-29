import { HttpInterceptorFn } from '@angular/common/http';
import { throwError } from 'rxjs/internal/observable/throwError';
import { catchError } from 'rxjs/internal/operators/catchError';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token') ?? 'mock-jwt-token';

  const clonedRequest = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  return next(clonedRequest).pipe(
    catchError((error) => {
      console.error('API Error:', error);

      return throwError(() => error);
    }),
  );
};
