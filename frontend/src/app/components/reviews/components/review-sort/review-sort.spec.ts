import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReviewSort } from './review-sort';

describe('ReviewSort', () => {
  let component: ReviewSort;
  let fixture: ComponentFixture<ReviewSort>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewSort],
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewSort);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
