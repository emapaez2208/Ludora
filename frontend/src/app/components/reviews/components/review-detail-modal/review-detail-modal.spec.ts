import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReviewDetailModal } from './review-detail-modal';

describe('ReviewDetailModal', () => {
  let component: ReviewDetailModal;
  let fixture: ComponentFixture<ReviewDetailModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewDetailModal],
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewDetailModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
