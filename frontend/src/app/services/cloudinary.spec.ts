import { TestBed } from '@angular/core/testing';
import { Cloudinary } from './cloudinary';

describe('Cloudinary', () => {
  let service: Cloudinary;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Cloudinary);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
