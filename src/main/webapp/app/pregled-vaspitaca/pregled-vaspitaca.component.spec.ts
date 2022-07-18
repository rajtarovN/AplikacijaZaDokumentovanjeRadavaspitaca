import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PregledVaspitacaComponent } from './pregled-vaspitaca.component';

describe('PregledVaspitacaComponent', () => {
  let component: PregledVaspitacaComponent;
  let fixture: ComponentFixture<PregledVaspitacaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PregledVaspitacaComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PregledVaspitacaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
