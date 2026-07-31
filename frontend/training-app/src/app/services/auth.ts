import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';

export interface User {
  username: string;
  password: string;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class Auth {
  private currentUser = signal<User | null>(null);
  private router = inject(Router);

  user = this.currentUser.asReadonly();

  login(email: string, password: string): void {
    console.log('Logging in with:', email, password);

    const mockUser: User = {
      username: email,
      password: password,
      role: 'admin',
    };

    this.currentUser.set(mockUser);
    this.router.navigate(['/home']);
  }

  logout(): void {
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return this.currentUser() !== null;
  }

  getToken(): string | null {
    return 'mock-jwt-token';
  }
}
