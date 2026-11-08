import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SalesDetails } from './sales-details';

describe('SalesDetails', () => {
  let component: SalesDetails;
  let fixture: ComponentFixture<SalesDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SalesDetails],
    }).compileComponents();

    fixture = TestBed.createComponent(SalesDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
