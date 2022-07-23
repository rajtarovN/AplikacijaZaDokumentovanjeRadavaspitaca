import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PisanjePlanaComponent } from './pisanje-plana.component';

describe('PisanjePlanaComponent', () => {
  let component: PisanjePlanaComponent;
  let fixture: ComponentFixture<PisanjePlanaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PisanjePlanaComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PisanjePlanaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
