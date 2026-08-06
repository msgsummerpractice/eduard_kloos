import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthResponse, JwtPayload, User } from '../models/auth.model';
import { jwtDecode } from 'jwt-decode';

@Injectable({ providedIn: 'root' })
export class Auth {
  private http = inject(HttpClient);
  private router = inject(Router);
  private currentUser = signal<User | null>(null);
  user = this.currentUser.asReadonly();
  private apiUrl =
    'https://backend.yellowground-4213ad35.germanynorth.azurecontainerapps.io/api/auth';

  constructor() {
    this.restoreUser();
  }

  login(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, { email, password });
  }

  verifyMfa(email: string, code: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/verify-mfa`, { email, code });
  }

  restoreUser(): void {
    const decoded = this.getDecodedToken();

    if (!decoded) {
      return;
    }

    this.currentUser.set({
      username: decoded.sub,
      roles: decoded.roles,
    });
  }

  saveToken(token: string): void {
    localStorage.setItem('token', token);
    this.restoreUser();
  }

  logout(): void {
    localStorage.removeItem('token');
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    const decoded = this.getDecodedToken();

    return !!decoded?.exp && decoded.exp * 1000 > Date.now();
  }

  setUser(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
  }

  private getDecodedToken(): JwtPayload | null {
    const token = localStorage.getItem('token');

    if (!token) {
      return null;
    }

    try {
      return jwtDecode<JwtPayload>(token);
    } catch {
      return null;
    }
  }
}
