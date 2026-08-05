import { Routes } from '@angular/router';
import { NotFoundComponent } from './not-found-component/not-found-component';
import { HomeComponent } from './home-component/home-component';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [authGuard],
  },

  {
    path: 'login',
    loadComponent: () => import('./login-component/login-component').then((m) => m.LoginComponent),
  },

  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },

  {
    path: '**',
    redirectTo: 'home',
  },
];
