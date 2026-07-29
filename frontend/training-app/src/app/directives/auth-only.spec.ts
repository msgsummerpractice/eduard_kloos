import { AuthOnly } from './auth-only';

describe('AuthOnly', () => {
  it('should create an instance', () => {
    const directive = new AuthOnly();
    expect(directive).toBeTruthy();
  });
});
