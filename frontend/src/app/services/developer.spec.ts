import { TestBed } from '@angular/core/testing';
import { Developer } from './developer';

describe('Developer', () => {
  let service: Developer;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Developer);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
