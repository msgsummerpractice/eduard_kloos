export interface User {
  username: string;
  roles: string[];
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface MfaRequest {
  email: string;
  code: string;
}

export interface AuthResponse {
  token: string;
  roles: string[];
  mfaRequired: boolean;
}
