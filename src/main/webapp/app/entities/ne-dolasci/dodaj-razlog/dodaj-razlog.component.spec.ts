import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DodajRazlogComponent } from './dodaj-razlog.component';

describe('DodajRazlogComponent', () => {
  let component: DodajRazlogComponent;
  let fixture: ComponentFixture<DodajRazlogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DodajRazlogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DodajRazlogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
