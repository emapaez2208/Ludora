import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdminChangeEmailModal } from './admin-change-email-modal';

describe('AdminChangeEmailModal', () => {
  let component: AdminChangeEmailModal;
  let fixture: ComponentFixture<AdminChangeEmailModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminChangeEmailModal],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminChangeEmailModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
