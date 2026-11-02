import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CategorySlide } from './category-slide';

describe('CategorySlide', () => {
  let component: CategorySlide;
  let fixture: ComponentFixture<CategorySlide>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategorySlide],
    }).compileComponents();

    fixture = TestBed.createComponent(CategorySlide);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
