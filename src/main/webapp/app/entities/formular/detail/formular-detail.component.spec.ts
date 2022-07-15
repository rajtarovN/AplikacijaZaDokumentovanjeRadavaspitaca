import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormularDetailComponent } from './formular-detail.component';

describe('Formular Management Detail Component', () => {
  let comp: FormularDetailComponent;
  let fixture: ComponentFixture<FormularDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormularDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ formular: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FormularDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FormularDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load formular on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.formular).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
