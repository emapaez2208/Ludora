import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GameDetail } from './game-detail';

describe('GameDetail', () => {
  let component: GameDetail;
  let fixture: ComponentFixture<GameDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(GameDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
