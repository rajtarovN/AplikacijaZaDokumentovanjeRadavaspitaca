import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PregledObjekataComponent } from './pregled-objekata.component';

describe('PregledObjekataComponent', () => {
  let component: PregledObjekataComponent;
  let fixture: ComponentFixture<PregledObjekataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PregledObjekataComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PregledObjekataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
