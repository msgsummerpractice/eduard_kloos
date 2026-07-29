import { Routes } from '@angular/router';
import { LoginComponent } from './login-component/login-component';
import { NotFoundComponent } from './not-found-component/not-found-component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: '**',
    component: NotFoundComponent,
  },
];
