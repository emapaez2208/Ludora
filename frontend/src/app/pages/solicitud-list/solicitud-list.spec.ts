import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SolicitudList } from './solicitud-list';

describe('SolicitudList', () => {
  let component: SolicitudList;
  let fixture: ComponentFixture<SolicitudList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SolicitudList],
    }).compileComponents();

    fixture = TestBed.createComponent(SolicitudList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
