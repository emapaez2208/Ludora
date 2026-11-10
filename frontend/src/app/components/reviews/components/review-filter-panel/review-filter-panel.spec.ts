import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReviewFilterPanel } from './review-filter-panel';

describe('ReviewFilterPanel', () => {
  let component: ReviewFilterPanel;
  let fixture: ComponentFixture<ReviewFilterPanel>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewFilterPanel],
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewFilterPanel);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
