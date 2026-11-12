import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UserEdit } from './user-edit';

describe('UserEdit', () => {
  let component: UserEdit;
  let fixture: ComponentFixture<UserEdit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserEdit],
    }).compileComponents();

    fixture = TestBed.createComponent(UserEdit);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
