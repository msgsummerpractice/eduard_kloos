import { Component, inject } from '@angular/core';
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
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
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

  onSubmit(): void {
    if (this.loginFormGroup.invalid) {
      this.loginFormGroup.markAllAsTouched();
      return;
    }

    this.login();
  }

  login(): void {
    const { email, password } = this.loginFormGroup.getRawValue();
    this.authService.login(email, password);
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
}
