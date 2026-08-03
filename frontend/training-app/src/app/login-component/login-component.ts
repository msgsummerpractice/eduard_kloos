import { Component, inject, signal } from '@angular/core';
import { Auth } from '../services/auth';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {
  FormControl,
  FormGroup,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';

type LoginForm = FormGroup<{
  email: FormControl<string>;
  password: FormControl<string>;
}>;

@Component({
  selector: 'app-login-component',
  standalone: true,
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css',
})
export class LoginComponent {
  private authService = inject(Auth);
  private readonly formBuilder = inject(NonNullableFormBuilder);
  protected readonly loginFormGroup: LoginForm = this.formBuilder.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
  });
  private router = inject(Router);
  showMfa = false;
  private emailForMfa = '';
  protected loginError = '';
  protected isLoading = signal(false);

  onSubmit(): void {
    this.loginError = '';
    if (this.loginFormGroup.invalid) {
      this.loginFormGroup.markAllAsTouched();
      return;
    }

    const { email, password } = this.loginFormGroup.getRawValue();
    this.isLoading.set(true);

    this.authService.login(email, password).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.emailForMfa = email;
        this.showMfa = true;
      },
      error: (err) => {
        this.isLoading.set(false);
        if (err.status === 401) {
          this.loginError = 'Invalid email or password.';
        } else {
          this.loginError = 'Something went wrong. Please try again.';
        }
      },
    });
  }

  logout(): void {
    this.authService.logout();
  }

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  get email() {
    return this.loginFormGroup.get('email');
  }

  get password() {
    return this.loginFormGroup.get('password');
  }

  verifyMfa(code: string): void {
    this.authService.verifyMfa(this.emailForMfa, code).subscribe({
      next: (response) => {
        this.authService.saveToken(response.token);
        this.router.navigate(['/home']);
      },
    });
  }
}
