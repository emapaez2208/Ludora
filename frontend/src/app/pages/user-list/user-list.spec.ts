import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { UserList } from './user-list';
 
describe('UserList', () => {
  let component: UserList;
  let fixture: ComponentFixture<UserList>;
 
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserList],
      providers: [provideHttpClient(), provideRouter([])],
    }).compileComponents();
 
    fixture = TestBed.createComponent(UserList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });
 
  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
 