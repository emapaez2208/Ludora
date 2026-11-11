import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdminChangePasswordModal } from './admin-change-password-modal';

describe('AdminChangePasswordModal', () => {
  let component: AdminChangePasswordModal;
  let fixture: ComponentFixture<AdminChangePasswordModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminChangePasswordModal],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminChangePasswordModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
