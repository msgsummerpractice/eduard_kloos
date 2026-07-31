import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { AuthResponse, User } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class Auth {
  private http = inject(HttpClient);
  private router = inject(Router);
  private currentUser = signal<User | null>(null);
  user = this.currentUser.asReadonly();
  private apiUrl = 'http://localhost:8081/api/auth';

  constructor() {
    const storedUser = localStorage.getItem('user');

    if (storedUser) {
      this.currentUser.set(JSON.parse(storedUser));
    }
  }

  login(email: string, password: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/login`, { email, password });
  }

  verifyMfa(email: string, code: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/verify-mfa`, {
      email,
      code,
    });
  }

  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return localStorage.getItem('token') !== null;
  }

  setUser(user: User): void {
    this.currentUser.set(user);
    localStorage.setItem('user', JSON.stringify(user));
  }
}
