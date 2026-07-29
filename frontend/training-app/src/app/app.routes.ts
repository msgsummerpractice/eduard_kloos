import { Routes } from '@angular/router';
import { NotFoundComponent } from './not-found-component/not-found-component';
import { HomeComponent } from './home-component/home-component';

export const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
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
    component: NotFoundComponent,
  },
];
