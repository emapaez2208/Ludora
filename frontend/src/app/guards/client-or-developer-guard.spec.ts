import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';
import { clientOrDeveloperGuard } from './client-or-developer-guard';

describe('clientOrDeveloperGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
    TestBed.runInInjectionContext(() => clientOrDeveloperGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
