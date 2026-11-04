import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeEmailModal } from './change-email-modal';

describe('ChangeEmailModal', () => {
  let component: ChangeEmailModal;
  let fixture: ComponentFixture<ChangeEmailModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChangeEmailModal],
    }).compileComponents();

    fixture = TestBed.createComponent(ChangeEmailModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
