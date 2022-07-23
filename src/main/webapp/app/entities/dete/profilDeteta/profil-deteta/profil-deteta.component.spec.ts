import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfilDetetaComponent } from './profil-deteta.component';

describe('ProfilDetetaComponent', () => {
  let component: ProfilDetetaComponent;
  let fixture: ComponentFixture<ProfilDetetaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProfilDetetaComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfilDetetaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
