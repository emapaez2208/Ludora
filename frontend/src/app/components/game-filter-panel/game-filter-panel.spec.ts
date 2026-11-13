import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GameFilterPanel } from './game-filter-panel';

describe('GameFilterPanel', () => {
  let component: GameFilterPanel;
  let fixture: ComponentFixture<GameFilterPanel>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameFilterPanel],
    }).compileComponents();

    fixture = TestBed.createComponent(GameFilterPanel);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
