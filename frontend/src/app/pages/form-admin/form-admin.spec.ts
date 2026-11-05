import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormAdmin } from './form-admin';

describe('FormAdmin', () => {
  let component: FormAdmin;
  let fixture: ComponentFixture<FormAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormAdmin],
    }).compileComponents();

    fixture = TestBed.createComponent(FormAdmin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
