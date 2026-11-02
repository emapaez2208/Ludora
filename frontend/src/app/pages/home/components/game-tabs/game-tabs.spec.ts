import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GameTabs } from './game-tabs';

describe('GameTabs', () => {
  let component: GameTabs;
  let fixture: ComponentFixture<GameTabs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameTabs],
    }).compileComponents();

    fixture = TestBed.createComponent(GameTabs);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
