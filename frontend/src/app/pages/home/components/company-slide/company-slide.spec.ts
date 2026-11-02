import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CompanySlide } from './company-slide';

describe('CompanySlide', () => {
  let component: CompanySlide;
  let fixture: ComponentFixture<CompanySlide>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompanySlide],
    }).compileComponents();

    fixture = TestBed.createComponent(CompanySlide);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
