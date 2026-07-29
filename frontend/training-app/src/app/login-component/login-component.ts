import { Component, inject } from '@angular/core';
import { Auth } from '../services/auth';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-login-component',
  imports: [MatButtonModule],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css',
})
export class LoginComponent {
  private authService = inject(Auth);

  login(): void {
    this.authService.login();
  }

  logout(): void {
    this.authService.logout();
  }

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }
}
