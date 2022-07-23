import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PregledZapazanjaComponent } from './pregled-zapazanja.component';

describe('PregledZapazanjaComponent', () => {
  let component: PregledZapazanjaComponent;
  let fixture: ComponentFixture<PregledZapazanjaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PregledZapazanjaComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PregledZapazanjaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
