import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GameSort } from './game-sort';

describe('GameSort', () => {
  let component: GameSort;
  let fixture: ComponentFixture<GameSort>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameSort],
    }).compileComponents();

    fixture = TestBed.createComponent(GameSort);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
