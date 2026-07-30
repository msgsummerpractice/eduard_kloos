import { Inject, Injectable, Service, signal } from '@angular/core';

export interface User {
  username: string;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class Auth {
  private currentUser = signal<User | null>(null);

  user = this.currentUser.asReadonly();

  login(): void {
    const mockUser: User = {
      username: 'John Doe',
      role: 'admin',
    };
    this.currentUser.set(mockUser);
  }

  logout(): void {
    this.currentUser.set(null);
  }

  isAuthenticated(): boolean {
    return this.currentUser() !== null;
  }

  getToken(): string | null {
    return 'mock-jwt-token';
  }
}
