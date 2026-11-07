import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SaleFilterPanel } from './sale-filter-panel';

describe('SaleFilterPanel', () => {
  let component: SaleFilterPanel;
  let fixture: ComponentFixture<SaleFilterPanel>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SaleFilterPanel],
    }).compileComponents();

    fixture = TestBed.createComponent(SaleFilterPanel);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
