import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IzostanciComponent } from './izostanci.component';

describe('IzostanciComponent', () => {
  let component: IzostanciComponent;
  let fixture: ComponentFixture<IzostanciComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [IzostanciComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IzostanciComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
