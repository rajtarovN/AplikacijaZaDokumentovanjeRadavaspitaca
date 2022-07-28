import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RasporedDeceComponent } from './raspored-dece.component';

describe('RasporedDeceComponent', () => {
  let component: RasporedDeceComponent;
  let fixture: ComponentFixture<RasporedDeceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RasporedDeceComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RasporedDeceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
