import { Component, inject } from '@angular/core';
import { AuthOnly } from './directives/auth-only';
import { Auth } from './services/auth';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { UsernamePipe } from './pipes/username-pipe';

@Component({
  selector: 'app-root',
  imports: [
    AuthOnly,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
    UsernamePipe,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  authService = inject(Auth);

  logout(): void {
    this.authService.logout();
  }
}
